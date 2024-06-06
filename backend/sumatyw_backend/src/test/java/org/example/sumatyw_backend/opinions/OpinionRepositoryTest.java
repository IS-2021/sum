package org.example.sumatyw_backend.opinions;

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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OpinionRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private OpinionRepository opinionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @AfterEach
    void tearDown() {
        opinionRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void findByUserUserIdAndRestaurantRestaurantId_ReturnsUserOpinionAboutRestaurant() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);
        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);
        Restaurant r3 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r3 = restaurantRepository.save(r3);

        List<Opinion> opinions = Arrays.asList(
            Opinion.builder().user(u1).restaurant(r1).build(),
            Opinion.builder().user(u1).restaurant(r2).build(),
            Opinion.builder().user(u2).restaurant(r3).build()
        );
        opinionRepository.saveAll(opinions);

        // when
        Optional<Opinion> userOpinion = opinionRepository.findByUserUserIdAndRestaurantRestaurantId(u1.getUserId(), r2.getRestaurantId());

        // then
        assertThat(userOpinion.isPresent()).isTrue();
        assertThat(userOpinion.get().getUser()).isEqualTo(u1);
        assertThat(userOpinion.get().getRestaurant()).isEqualTo(r2);
    }

    @Test
    void findByUserUserIdAndRestaurantRestaurantId_ReturnsEmptyOptional_IfGivenUserDoesNotHaveOpinionAboutGivenRestaurant() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);
        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);
        Restaurant r3 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r3 = restaurantRepository.save(r3);

        List<Opinion> opinions = Arrays.asList(
            Opinion.builder().user(u1).restaurant(r1).build(),
            Opinion.builder().user(u1).restaurant(r2).build(),
            Opinion.builder().user(u2).restaurant(r3).build()
        );
        opinionRepository.saveAll(opinions);

        // when
        Optional<Opinion> userOpinion = opinionRepository.findByUserUserIdAndRestaurantRestaurantId(UUID.randomUUID(), r2.getRestaurantId());

        // then
        assertThat(userOpinion.isPresent()).isFalse();
    }

    @Test
    void findByUserUserIdAndRestaurantRestaurantId_ReturnsEmptyOptional_IfGivenRestaurantDoesNotHaveAnyOpinion() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);
        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);
        Restaurant r3 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r3 = restaurantRepository.save(r3);

        List<Opinion> opinions = Arrays.asList(
            Opinion.builder().user(u1).restaurant(r1).build(),
            Opinion.builder().user(u1).restaurant(r2).build(),
            Opinion.builder().user(u2).restaurant(r3).build()
        );
        opinionRepository.saveAll(opinions);

        // when
        Optional<Opinion> userOpinion = opinionRepository.findByUserUserIdAndRestaurantRestaurantId(u1.getUserId(), UUID.randomUUID());

        // then
        assertThat(userOpinion.isPresent()).isFalse();
    }

    @Test
    void findAllByRestaurantRestaurantId_ReturnsAllRestaurantOpinions() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());
        User u3 = userRepository.save(User.builder().build());
        User u4 = userRepository.save(User.builder().build());


        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);
        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);

        List<Opinion> opinions = Arrays.asList(
            Opinion.builder().user(u1).restaurant(r1).build(),
            Opinion.builder().user(u1).restaurant(r2).build(),
            Opinion.builder().user(u2).restaurant(r1).build(),
            Opinion.builder().user(u3).restaurant(r1).build(),
            Opinion.builder().user(u4).restaurant(r1).build()
        );
        opinionRepository.saveAll(opinions);

        // when
        List<Opinion> restaurantOpinions = opinionRepository.findAllByRestaurantRestaurantId(r1.getRestaurantId());

        // then
        assertThat(restaurantOpinions.size()).isEqualTo(4);

        for (Opinion o : restaurantOpinions) {
            assertThat(o.getRestaurant()).isEqualTo(r1);
        }
    }

}
