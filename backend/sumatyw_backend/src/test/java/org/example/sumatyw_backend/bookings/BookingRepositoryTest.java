package org.example.sumatyw_backend.bookings;

import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.meals.MealRepository;
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
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final Sort sort = Sort.by(Sort.Order.desc("timestamp"));

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
        mealRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void findBookingByMeal_RestaurantRestaurantId_ReturnsAllRestaurantDescSortedByTimestapmBookings() {
        // given
        User user = new User();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant2 = restaurantRepository.save(restaurant2);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant2).build();
        meal2 = mealRepository.save(meal2);


        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).timestamp(LocalDateTime.now().minusDays(3)).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).timestamp(LocalDateTime.now().minusDays(7)).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        List<Booking> restaurant1Bookings = bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurant1.getRestaurantId(), sort);

        // then
        assertThat(restaurant1Bookings.size()).isEqualTo(2);
        assertThat(restaurant1Bookings.get(0).getMeal().getRestaurant()).isEqualTo(restaurant1);
        assertThat(restaurant1Bookings.get(1).getMeal().getRestaurant()).isEqualTo(restaurant1);
        assertThat(restaurant1Bookings.get(0).getTimestamp().isAfter(restaurant1Bookings.get(1).getTimestamp())).isTrue();
    }

    @Test
    void findByUserUserIdAndStatus_ReturnsUserBookingWithGivenStatus() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant1).build();
        meal2 = mealRepository.save(meal2);

        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        Optional<Booking> foundBooking = bookingRepository.findByUserUserIdAndStatus(user.getUserId(), Status.PickedUp);

        // then
        assertThat(foundBooking.isPresent()).isTrue();
        assertThat(foundBooking.get().getUser()).isEqualTo(user);
        assertThat(foundBooking.get().getStatus()).isEqualTo(Status.PickedUp);
    }

    @Test
    void findByUserUserIdAndStatus_ReturnsEmptyOptional_IfBookingsWithGivenUserDoNotExist() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant1).build();
        meal2 = mealRepository.save(meal2);

        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        Optional<Booking> foundBooking = bookingRepository.findByUserUserIdAndStatus(UUID.randomUUID(), Status.PickedUp);

        // then
        assertThat(foundBooking.isPresent()).isFalse();
    }

    @Test
    void findByUserUserIdAndStatus_ReturnsEmptyOptional_IfUserBookingWithGivenStatusDoNotExist() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant1).build();
        meal2 = mealRepository.save(meal2);

        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        Optional<Booking> foundBooking = bookingRepository.findByUserUserIdAndStatus(user.getUserId(), Status.OutOfDate);

        // then
        assertThat(foundBooking.isPresent()).isFalse();
    }


    @Test
    void findByMealMealIdAndStatus_ReturnsMealBookingWithGivenStatus() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant1).build();
        meal2 = mealRepository.save(meal2);

        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        Optional<Booking> foundBooking = bookingRepository.findByMealMealIdAndStatus(meal1.getMealId(), Status.PickedUp);

        // then
        assertThat(foundBooking.isPresent()).isTrue();
        assertThat(foundBooking.get().getMeal()).isEqualTo(meal1);
        assertThat(foundBooking.get().getStatus()).isEqualTo(Status.PickedUp);
    }

    @Test
    void findByMealMealIdAndStatus_ReturnsEmptyOptional_IfBookingsWithGivenMealDoNotExist() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant1).build();
        meal2 = mealRepository.save(meal2);

        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        Optional<Booking> foundBooking = bookingRepository.findByMealMealIdAndStatus(UUID.randomUUID(), Status.PickedUp);

        // then
        assertThat(foundBooking.isPresent()).isFalse();
    }

    @Test
    void findByMealMealIdAndStatus_ReturnsEmptyOptional_IfMealBookingWithGivenStatusDoNotExist() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal1).status(Status.Cancelled).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        Optional<Booking> foundBooking = bookingRepository.findByMealMealIdAndStatus(meal1.getMealId(), Status.OutOfDate);

        // then
        assertThat(foundBooking.isPresent()).isFalse();
    }

    @Test
    void findAllByMealMealId_ReturnsAllMealBookings() {
        // given
        User user = new User();
        user = userRepository.save(user);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant2 = restaurantRepository.save(restaurant2);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant2).build();
        meal2 = mealRepository.save(meal2);


        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user).meal(meal1).status(Status.Active).build(),
            Booking.builder().user(user).meal(meal1).status(Status.PickedUp).build(),
            Booking.builder().user(user).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        List<Booking> meal1Bookings = bookingRepository.findAllByMealMealId(meal1.getMealId());

        // then
        assertThat(meal1Bookings.size()).isEqualTo(2);
        assertThat(meal1Bookings.get(0).getMeal()).isEqualTo(meal1);
        assertThat(meal1Bookings.get(1).getMeal()).isEqualTo(meal1);
    }

    @Test
    void findAllByUserUserId_ReturnsAllUserDescSortedByTimestampBookings() {
        // given
        User user1 = new User();
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2 = userRepository.save(user2);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant2 = restaurantRepository.save(restaurant2);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant2).build();
        meal2 = mealRepository.save(meal2);


        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user1).meal(meal1).timestamp(LocalDateTime.now().minusDays(3)).status(Status.Active).build(),
            Booking.builder().user(user1).meal(meal1).timestamp(LocalDateTime.now().minusDays(7)).status(Status.PickedUp).build(),
            Booking.builder().user(user2).meal(meal2).status(Status.Cancelled).build(),
            Booking.builder().user(user2).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        List<Booking> user1Bookings = bookingRepository.findAllByUserUserId(user1.getUserId(), sort);

        // then
        assertThat(user1Bookings.size()).isEqualTo(2);
        assertThat(user1Bookings.get(0).getUser()).isEqualTo(user1);
        assertThat(user1Bookings.get(1).getUser()).isEqualTo(user1);
        assertThat(user1Bookings.get(0).getTimestamp().isAfter(user1Bookings.get(1).getTimestamp())).isTrue();
    }

    @Test
    void findByStatus_ReturnsAllDescSortedByTimestampBookingsWithGivenStatus() {
        // given
        User user1 = new User();
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2 = userRepository.save(user2);

        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant1 = restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        restaurant2 = restaurantRepository.save(restaurant2);

        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
        meal1 = mealRepository.save(meal1);

        Meal meal2 = Meal.builder().restaurant(restaurant2).build();
        meal2 = mealRepository.save(meal2);


        List<Booking> bookings = Arrays.asList(
            Booking.builder().user(user1).meal(meal1).timestamp(LocalDateTime.now().minusDays(3)).status(Status.Cancelled).build(),
            Booking.builder().user(user1).meal(meal1).timestamp(LocalDateTime.now().minusDays(7)).status(Status.Cancelled).build(),
            Booking.builder().user(user2).meal(meal2).status(Status.Active).build(),
            Booking.builder().user(user2).meal(meal2).status(Status.OutOfDate).build()
        );

        bookingRepository.saveAll(bookings);

        // when
        List<Booking> cancelledBookings = bookingRepository.findByStatus(Status.Cancelled, sort);

        // then
        assertThat(cancelledBookings.size()).isEqualTo(2);
        assertThat(cancelledBookings.get(0).getStatus()).isEqualTo(Status.Cancelled);
        assertThat(cancelledBookings.get(1).getStatus()).isEqualTo(Status.Cancelled);
        assertThat(cancelledBookings.get(0).getTimestamp().isAfter(cancelledBookings.get(1).getTimestamp())).isTrue();
    }
}
