package org.example.sumatyw_backend.restaurants;

import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressRepository;
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
class RestaurantRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @AfterEach
    void tearDown() {
        restaurantRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void findByPhoneNumber_ReturnsRestaurantWithGivenPhoneNumber() {
        // given
        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).phoneNumber("111222333").build();
        r1 = restaurantRepository.save(r1);

        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).phoneNumber("999888777").build();
        r2 = restaurantRepository.save(r2);

        // when
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByPhoneNumber(r1.getPhoneNumber());

        // then
        assertThat(foundRestaurant.isPresent()).isTrue();
        assertThat(foundRestaurant.get().getPhoneNumber()).isEqualTo(r1.getPhoneNumber());
    }

    @Test
    void findByPhoneNumber_ReturnsEmptyOptional_IfRestaurantWithGivenPhoneNumberDoesNotExist() {
        // given
        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).phoneNumber("111222333").build();
        r1 = restaurantRepository.save(r1);

        Restaurant r2 = Restaurant.builder().restaurantId(UUID.randomUUID()).phoneNumber("999888777").build();
        r2 = restaurantRepository.save(r2);

        // when
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByPhoneNumber("123456789");

        // then
        assertThat(foundRestaurant.isPresent()).isFalse();
    }

    @Test
    void findAllByAddress_City_AndStatus_ReturnsAllRestaurantWithGivenStatusFromGivenCity() {
        // given
        Address a1 = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        addressRepository.save(a1);
        Address a2 = Address.builder().addressId("fhgdsgtgtgresf").city("Lodz").build();
        addressRepository.save(a2);
        Address a3 = Address.builder().addressId("tujrhggdsgsfgs").city("Lodz").build();
        addressRepository.save(a3);
        Address a4 = Address.builder().addressId("heyrtwrtwrtwre").city("Warsaw").build();
        addressRepository.save(a4);
        Address a5 = Address.builder().addressId("ilujrhgrerthgt").city("Warsaw").build();
        addressRepository.save(a5);

        List<Restaurant> restaurants = Arrays.asList(
            Restaurant.builder().restaurantId(UUID.randomUUID()).address(a1).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).address(a2).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).address(a5).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).address(a3).status(RestaurantStatus.Banned).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).address(a4).status(RestaurantStatus.Active).build()
        );
        restaurantRepository.saveAll(restaurants);

        // when
        List<Restaurant> inactiveRestaurants = restaurantRepository.findAllByAddress_City_AndStatus(a1.getCity(), RestaurantStatus.Active);

        // then
        assertThat(inactiveRestaurants.size()).isEqualTo(2);

        for (Restaurant r : inactiveRestaurants) {
            assertThat(r.getAddress().getCity()).isEqualTo("Lodz");
        assertThat(r.getStatus()).isEqualTo(RestaurantStatus.Active);
        }
    }

    @Test
    void findAllByStatus_ReturnsAllRestaurantsWithGivenStatus() {
        // given
        List<Restaurant> restaurants = Arrays.asList(
            Restaurant.builder().restaurantId(UUID.randomUUID()).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).status(RestaurantStatus.Banned).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).status(RestaurantStatus.Active).build()
        );
        restaurantRepository.saveAll(restaurants);

        // when
        List<Restaurant> activeRestaurants = restaurantRepository.findAllByStatus(RestaurantStatus.Active);

        // then
        assertThat(activeRestaurants.size()).isEqualTo(4);

        for (Restaurant r : activeRestaurants) {
            assertThat(r.getStatus()).isEqualTo(RestaurantStatus.Active);
        }
    }
}
