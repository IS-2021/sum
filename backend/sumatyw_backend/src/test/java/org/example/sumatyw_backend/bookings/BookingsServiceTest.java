package org.example.sumatyw_backend.bookings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookingsServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    private UUID userId;
    private UUID mealId;
    private UUID bookingId;
    private UUID restaurantId;
    private Booking booking;
    private User user;
    private Meal meal;
    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        mealId = UUID.randomUUID();
        bookingId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        user = User.builder().userId(userId).build();
        meal = Meal.builder().mealId(mealId).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).build();
        restaurant = Restaurant.builder().restaurantId(restaurantId).build();
        booking = Booking.builder()
            .user(user)
            .meal(meal)
            .active(true)
            .build();
    }

    @Test
    void testCreateBooking_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserUserIdAndActiveIsTrue(userId)).thenReturn(Optional.empty());
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        when(bookingRepository.findByMealMealIdAndActiveIsTrue(mealId)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertEquals(booking, result);
        assertNotNull(result.getTimestamp());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    void testCreateBooking_ActiveBookingExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserUserIdAndActiveIsTrue(userId)).thenReturn(Optional.of(booking));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("User already have active booking", exception.getMessage());
    }

    @Test
    void testCreateBooking_MealNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserUserIdAndActiveIsTrue(userId)).thenReturn(Optional.empty());
        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Meal not found with ID: " + mealId, exception.getMessage());
    }

    @Test
    void testCreateBooking_MealAlreadyBooked() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserUserIdAndActiveIsTrue(userId)).thenReturn(Optional.empty());
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        when(bookingRepository.findByMealMealIdAndActiveIsTrue(mealId)).thenReturn(Optional.of(booking));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("This meal is already booked", exception.getMessage());
    }

    @Test
    void testGetBookingById_Success() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(bookingId);

        assertEquals(booking, result);
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getBookingById(bookingId);
        });

        assertEquals("Booking not found with ID: " + bookingId, exception.getMessage());
    }

    @Test
    void testGetBookingByUserId_Success() {
        when(bookingRepository.findByUserUserIdAndActiveIsTrue(userId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingByUserId(userId);

        assertEquals(booking, result);
    }

    @Test
    void testGetBookingByUserId_NotFound() {
        when(bookingRepository.findByUserUserIdAndActiveIsTrue(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getBookingByUserId(userId);
        });

        assertEquals("Active booking not found with user ID: " + userId, exception.getMessage());
    }

    @Test
    void testGetAllUserBookings_Success() {
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findAllByUserUserId(userId)).thenReturn(bookings);

        List<Booking> result = bookingService.getAllUserBookings(userId);

        assertEquals(bookings, result);
    }

    @Test
    void testDeleteBookingById_Success() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.deleteBookingById(bookingId);

        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    void testDeleteBookingById_NotFound() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.deleteBookingById(bookingId);
        });

        assertEquals("Booking not found with ID: " + bookingId, exception.getMessage());
    }

    @Test
    void testGetAllActiveBookings_Success() {
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingByActiveIsTrue()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllActiveBookings();

        assertEquals(bookings, result);
    }

    @Test
    void testGetBookingsByRestaurantID_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByRestaurantID(restaurantId);

        assertEquals(bookings, result);
    }

    @Test
    void testGetBookingsByRestaurantID_RestaurantNotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getBookingsByRestaurantID(restaurantId);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());
    }

    @Test
    void testGetActiveBookingsByRestaurantID_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId)).thenReturn(bookings);

        List<Booking> result = bookingService.getActiveBookingsByRestaurantID(restaurantId, true);

        assertEquals(bookings, result);
    }

    @Test
    void testGetActiveBookingsByRestaurantID_RestaurantNotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getActiveBookingsByRestaurantID(restaurantId, true);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());
    }

    @Test
    void testMarkBookingAsPickedUp_Success() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.markBookingAsPickedUp(bookingId, booking);

        assertNotNull(result.getPickedUpTimestamp());
        assertFalse(result.isActive());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testMarkBookingAsPickedUp_NotFound() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.markBookingAsPickedUp(bookingId, booking);
        });

        assertEquals("Booking not found with ID: " + bookingId, exception.getMessage());
    }
}

