//package org.example.sumatyw_backend.bookings;
//
//import org.example.sumatyw_backend.meals.Meal;
//import org.example.sumatyw_backend.meals.MealRepository;
//import org.example.sumatyw_backend.restaurants.Restaurant;
//import org.example.sumatyw_backend.restaurants.RestaurantRepository;
//import org.example.sumatyw_backend.users.User;
//import org.example.sumatyw_backend.users.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.testcontainers.containers.MariaDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class BookingRepositoryTest {
//
//    @Container
//    @ServiceConnection
//    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));
//
//    @Autowired
//    private BookingRepository bookingRepository;
//
//    @Autowired
//    private MealRepository mealRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RestaurantRepository restaurantRepository;
//
//    @AfterEach
//    void tearDown() {
//        bookingRepository.deleteAll();
//        userRepository.deleteAll();
//        restaurantRepository.deleteAll();
//        mealRepository.deleteAll();
//    }
//
//    @Test
//    void connectionTest(){
//        assertThat(mariaDB.isCreated()).isTrue();
//        assertThat(mariaDB.isRunning()).isTrue();
//    }
//
//    @Test
//    void findBookingByMeal_RestaurantRestaurantId_ReturnsAllRestaurantBookings() {
//        // given
//        User user = new User();
//        user = userRepository.save(user);
//
//        Restaurant restaurant1 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
//        restaurant1 = restaurantRepository.save(restaurant1);
//
//        Restaurant restaurant2 = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
//        restaurant2 = restaurantRepository.save(restaurant2);
//
//        Meal meal1 = Meal.builder().restaurant(restaurant1).build();
//        meal1 = mealRepository.save(meal1);
//
//        Meal meal2 = Meal.builder().restaurant(restaurant2).build();
//        meal2 = mealRepository.save(meal2);
//
//
//        List<Booking> bookings = Arrays.asList(
//            Booking.builder().user(user).meal(meal1).active(true).build(),
//            Booking.builder().user(user).meal(meal1).active(false).build(),
//            Booking.builder().user(user).meal(meal2).active(true).build(),
//            Booking.builder().user(user).meal(meal2).active(false).build()
//        );
//
//        bookingRepository.saveAll(bookings);
//
//        // when
//        List<Booking> restaurant1Bookings = bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurant1.getRestaurantId());
//
//        // then
//        assertThat(restaurant1Bookings.size()).isEqualTo(2);
//        assertThat(restaurant1Bookings.get(0).getMeal().getRestaurant()).isEqualTo(restaurant1);
//        assertThat(restaurant1Bookings.get(1).getMeal().getRestaurant()).isEqualTo(restaurant1);
//    }
//
//    @Test
//    void findByUserUserIdAndActiveIsTrue_ReturnsActiveUserBooking() {
//        // given
//        User user = new User();
//        user = userRepository.save(user);
//
//        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
//        restaurant = restaurantRepository.save(restaurant);
//
//        Meal meal = Meal.builder().restaurant(restaurant).build();
//        meal = mealRepository.save(meal);
//
//        bookingRepository.save(Booking.builder().user(user).meal(meal).active(false).build());
//        bookingRepository.save(Booking.builder().user(user).meal(meal).active(false).build());
//        UUID activeBookingId = bookingRepository.save(Booking.builder().user(user).meal(meal).active(true).build()).getBookedId();
//
//        // when
//        Optional<Booking> activeBooking = bookingRepository.findByUserUserIdAndActiveIsTrue(user.getUserId());
//
//        // then
//        assertThat(activeBooking.isPresent()).isTrue();
//        assertThat(activeBooking.get().isActive()).isTrue();
//        assertThat(activeBooking.get().getBookedId()).isEqualTo(activeBookingId);
//    }
//
//    @Test
//    void findByUserUserIdAndActiveIsTrue_ReturnsEmptyOptional_IfUserDoesNotHaveActiveBooking() {
//        // given
//        User user = new User();
//        user = userRepository.save(user);
//
//        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
//        restaurant = restaurantRepository.save(restaurant);
//
//        Meal meal = Meal.builder().restaurant(restaurant).build();
//        meal = mealRepository.save(meal);
//
//        bookingRepository.save(Booking.builder().user(user).meal(meal).active(false).build());
//        bookingRepository.save(Booking.builder().user(user).meal(meal).active(false).build());
//        bookingRepository.save(Booking.builder().user(user).meal(meal).active(false).build());
//
//        // when
//        Optional<Booking> activeBooking = bookingRepository.findByUserUserIdAndActiveIsTrue(user.getUserId());
//
//        // then
//        assertThat(activeBooking.isPresent()).isFalse();
//    }
//
//    @Test
//    void findBookingByActiveIsTrue_ReturnsOnlyActiveBookings() {
//        // given
//        User user = new User();
//        user = userRepository.save(user);
//
//        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
//        restaurant = restaurantRepository.save(restaurant);
//
//        Meal meal = Meal.builder().restaurant(restaurant).build();
//        meal = mealRepository.save(meal);
//
//
//        List<Booking> bookings = Arrays.asList(
//            Booking.builder().user(user).meal(meal).active(true).build(),
//            Booking.builder().user(user).meal(meal).active(true).build(),
//            Booking.builder().user(user).meal(meal).active(true).build(),
//            Booking.builder().user(user).meal(meal).active(false).build()
//        );
//
//        bookingRepository.saveAll(bookings);
//
//        // when
//        List<Booking> activeBookings = bookingRepository.findBookingByActiveIsTrue();
//
//        // then
//        assertThat(activeBookings.size()).isEqualTo(3);
//        for (Booking b : activeBookings) {
//            assertThat(b.isActive()).isTrue();
//        }
//    }
//}
