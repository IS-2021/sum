package org.example.sumatyw_backend.restaurant_reports;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RestaurantReportRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private RestaurantReportRepository restaurantReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @AfterEach
    void tearDown() {
        restaurantReportRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void existsByUserIdAndRestaurantId_ReturnsTrue_IfGivenRestaurantReportExists() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        RestaurantReport rr1 = RestaurantReport.builder().restaurant(r1).user(u1).isOpen(true).build();
        rr1 = restaurantReportRepository.save(rr1);

        // when
        boolean reportExists = restaurantReportRepository.existsByUserIdAndRestaurantId(u1.getUserId(), r1.getRestaurantId());

        // then
        assertThat(reportExists).isTrue();
    }

    @Test
    void existsByUserIdAndRestaurantId_ReturnsFalse_IfGivenRestaurantReportDoesNotExist() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        RestaurantReport rr1 = RestaurantReport.builder().restaurant(r1).user(u1).build();
        rr1 = restaurantReportRepository.save(rr1);

        // when
        boolean reportExists = restaurantReportRepository.existsByUserIdAndRestaurantId(UUID.randomUUID(), r1.getRestaurantId());

        // then
        assertThat(reportExists).isFalse();
    }

    @Test
    void existsByUserIdAndRestaurantId_ReturnsFalse_IfGivenRestaurantReportIsClosed() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        RestaurantReport rr1 = RestaurantReport.builder().restaurant(r1).user(u1).isOpen(false).build();
        rr1 = restaurantReportRepository.save(rr1);

        // when
        boolean reportExists = restaurantReportRepository.existsByUserIdAndRestaurantId(UUID.randomUUID(), r1.getRestaurantId());

        // then
        assertThat(reportExists).isFalse();
    }

    @Test
    void findByIsOpenIsTrue_ReturnsOnlyOpenedRestaurantReports() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        List<RestaurantReport> restaurantReports = Arrays.asList(
            RestaurantReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            RestaurantReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            RestaurantReport.builder().restaurant(r1).user(u1).isOpen(false).build()
        );
        restaurantReportRepository.saveAll(restaurantReports);

        // when
        List<RestaurantReport> openedReports = restaurantReportRepository.findByIsOpenIsTrue();

        // then
        assertThat(openedReports.size()).isEqualTo(2);

        for (RestaurantReport rr : openedReports) {
            assertThat(rr.isOpen()).isTrue();
        }
    }
}
