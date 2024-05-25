package org.example.sumatyw_backend.favourites;

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
class FavouriteRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        favouriteRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void findByUserUserId_ReturnsUserFavouritesRestaurants() {
        // given
        User u1 = userRepository.save(User.builder().build());
        User u2 = userRepository.save(User.builder().build());

        Restaurant r1 = restaurantRepository.save(Restaurant.builder().restaurantId(UUID.randomUUID()).build());
        Restaurant r2 = restaurantRepository.save(Restaurant.builder().restaurantId(UUID.randomUUID()).build());
        Restaurant r3 = restaurantRepository.save(Restaurant.builder().restaurantId(UUID.randomUUID()).build());
        Restaurant r4 = restaurantRepository.save(Restaurant.builder().restaurantId(UUID.randomUUID()).build());

        List<Favourite> favourites = Arrays.asList(
            Favourite.builder().user(u1).restaurant(r1).build(),
            Favourite.builder().user(u1).restaurant(r2).build(),
            Favourite.builder().user(u1).restaurant(r3).build(),
            Favourite.builder().user(u2).restaurant(r4).build()
        );

        favouriteRepository.saveAll(favourites);

        // when
        List<Favourite> userFavourites = favouriteRepository.findByUserUserId(u1.getUserId());

        // then
        assertThat(userFavourites.size()).isEqualTo(3);

        for (Favourite f : userFavourites) {
            assertThat(f.getUser().getUserId()).isEqualTo(u1.getUserId());
        }
    }

    @Test
    void findByUserUserIdAndRestaurantRestaurantId_ReturnsFavouriteRestaurant_IfGivenRestaurantExists() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = restaurantRepository.save(Restaurant.builder().restaurantId(UUID.randomUUID()).build());

        Favourite favourite = Favourite.builder().user(u1).restaurant(r1).build();
        favouriteRepository.save(favourite);

        // when
        Optional<Favourite> userFavourite = favouriteRepository
            .findByUserUserIdAndRestaurantRestaurantId(u1.getUserId(), r1.getRestaurantId());

        // then
        assertThat(userFavourite.isPresent()).isTrue();
        assertThat(userFavourite.get().getRestaurant().getRestaurantId()).isEqualTo(r1.getRestaurantId());
        assertThat(userFavourite.get().getUser().getUserId()).isEqualTo(u1.getUserId());
    }

    @Test
    void findByUserUserIdAndRestaurantRestaurantId_ReturnsEmptyOptional_IfGivenRestaurantDoesNotExist() {
        // given
        User u1 = userRepository.save(User.builder().build());

        Restaurant r1 = restaurantRepository.save(Restaurant.builder().restaurantId(UUID.randomUUID()).build());

        Favourite favourite = Favourite.builder().user(u1).restaurant(r1).build();
        favouriteRepository.save(favourite);

        // when
        Optional<Favourite> userFavourite = favouriteRepository
            .findByUserUserIdAndRestaurantRestaurantId(UUID.randomUUID(), r1.getRestaurantId());

        // then
        assertThat(userFavourite.isPresent()).isFalse();
    }
}
