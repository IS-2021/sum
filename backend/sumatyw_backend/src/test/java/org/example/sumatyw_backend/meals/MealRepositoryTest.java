package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
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
class MealRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @AfterEach
    void tearDown() {
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void findAllByRestaurantRestaurantId_ReturnsAllRestaurantMeals() {
        // given
        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r1 = restaurantRepository.save(r1);

        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        r2 = restaurantRepository.save(r2);

        List<Meal> meals = Arrays.asList(
            Meal.builder().restaurant(r1).build(),
            Meal.builder().restaurant(r1).build(),
            Meal.builder().restaurant(r2).build()
        );
        mealRepository.saveAll(meals);

        // when
        List<Meal> restaurantMeals = mealRepository.findAllByRestaurantRestaurantId(r1.getRestaurantId());

        // then
        assertThat(restaurantMeals.size()).isEqualTo(2);

        for (Meal m : restaurantMeals) {
            assertThat(m.getRestaurant().getRestaurantId()).isEqualTo(r1.getRestaurantId());
        }
    }
}
