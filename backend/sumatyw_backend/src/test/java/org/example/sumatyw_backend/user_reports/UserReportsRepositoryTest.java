package org.example.sumatyw_backend.user_reports;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
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
class UserReportsRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private UserReportsRepository userReportsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @AfterEach
    void tearDown() {
        userReportsRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void existsByUserIdAndRestaurantId_ReturnsTrue_IfGivenUserReportExists() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        UserReport ur1 = UserReport.builder().restaurant(r1).user(u1).isOpen(true).build();
        ur1 = userReportsRepository.save(ur1);

        // when
        boolean reportExists = userReportsRepository.existsByUserIdAndRestaurantId(u1.getUserId(), r1.getRestaurantId());

        // then
        assertThat(reportExists).isTrue();
    }

    @Test
    void existsByUserIdAndRestaurantId_ReturnsFalse_IfGivenUserReportDoesNotExist() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        UserReport ur1 = UserReport.builder().restaurant(r1).user(u1).build();
        ur1 = userReportsRepository.save(ur1);

        // when
        boolean reportExists = userReportsRepository.existsByUserIdAndRestaurantId(UUID.randomUUID(), r1.getRestaurantId());

        // then
        assertThat(reportExists).isFalse();
    }

    @Test
    void existsByUserIdAndRestaurantId_ReturnsFalse_IfGivenUserReportIsClosed() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        UserReport ur1 = UserReport.builder().restaurant(r1).user(u1).isOpen(false).build();
        ur1 = userReportsRepository.save(ur1);

        // when
        boolean reportExists = userReportsRepository.existsByUserIdAndRestaurantId(UUID.randomUUID(), r1.getRestaurantId());

        // then
        assertThat(reportExists).isFalse();
    }

    @Test
    void findByIsOpenIsTrue_ReturnsOnlyOpenedUserReports() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r1).user(u1).isOpen(false).build()
        );
        userReportsRepository.saveAll(userReports);

        // when
        List<UserReport> openedReports = userReportsRepository.findByIsOpenIsTrue();

        // then
        assertThat(openedReports.size()).isEqualTo(2);

        for (UserReport ur : openedReports) {
            assertThat(ur.isOpen()).isTrue();
        }
    }

//    ////////////////////////////////////////////////////////////
    @Test
    void findAllByUserUserIdAndIsOpenTrue_ReturnsAllUserOpenedReports() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r2).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r1).user(u1).isOpen(false).build(),
            UserReport.builder().restaurant(r2).user(u2).isOpen(true).build()
        );
        userReportsRepository.saveAll(userReports);

        // when
        List<UserReport> userOpenedReports = userReportsRepository.findAllByUserUserIdAndIsOpenTrue(u1.getUserId());

        // then
        assertThat(userOpenedReports.size()).isEqualTo(2);

        for (UserReport ur : userOpenedReports) {
            assertThat(ur.getUser()).isEqualTo(u1);
            assertThat(ur.isOpen()).isTrue();
        }
    }

    @Test
    void findAllByRestaurantRestaurantIdAndIsOpenTrue_ReturnsAllRestaurantOpenedReports() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r2).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r1).user(u1).isOpen(false).build(),
            UserReport.builder().restaurant(r1).user(u2).isOpen(true).build()
        );
        userReportsRepository.saveAll(userReports);

        // when
        List<UserReport> userOpenedReports = userReportsRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(r1.getRestaurantId());

        // then
        assertThat(userOpenedReports.size()).isEqualTo(2);

        for (UserReport ur : userOpenedReports) {
            assertThat(ur.getRestaurant()).isEqualTo(r1);
            assertThat(ur.isOpen()).isTrue();
        }
    }

    @Test
    void findAllByUserUserIdAndRestaurantRestaurantIdAndIsOpenTrue_ReturnsUserReportForRestaurant() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().restaurant(r1).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r2).user(u1).isOpen(true).build(),
            UserReport.builder().restaurant(r1).user(u1).isOpen(false).build(),
            UserReport.builder().restaurant(r1).user(u2).isOpen(true).build()
        );
        userReportsRepository.saveAll(userReports);

        // when
        List<UserReport> userOpenedReports = userReportsRepository.findAllByRestaurantRestaurantIdAndUserUserIdAndIsOpenTrue(r1.getRestaurantId(), u1.getUserId());

        // then
        assertThat(userOpenedReports.size()).isEqualTo(1);

        for (UserReport ur : userOpenedReports) {
            assertThat(ur.getRestaurant()).isEqualTo(r1);
            assertThat(ur.getUser()).isEqualTo(u1);
            assertThat(ur.isOpen()).isTrue();
        }
    }
}
