package org.example.sumatyw_backend.bookings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.sumatyw_backend.exceptions.InvalidDataException;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.restaurants.RestaurantStatus;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Meal meal;
    private Booking booking;
    private UUID userId;
    private UUID mealId;
    private UUID bookingId;
    private UUID restaurantId;
    private Restaurant restaurant;
    Sort sort = Sort.by(Sort.Order.desc("timestamp"));


    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mealId = UUID.randomUUID();
        bookingId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();

        restaurant = Restaurant.builder().restaurantId(restaurantId).status(RestaurantStatus.Active).build();


        user = User.builder().userId(userId).build();
        meal = Meal.builder().mealId(mealId).restaurant(restaurant).build();
        booking = Booking.builder()
            .bookedId(bookingId)
            .user(user)
            .meal(meal)
            .status(Status.Active)
            .timestamp(LocalDateTime.now())
            .build();
    }

    @Test
    void testCreateBooking_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result);
        assertEquals(Status.Active, result.getStatus());
        verify(userRepository).findById(userId);
        verify(mealRepository).findById(mealId);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCreateBooking_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            bookingService.createBooking(booking);
        });

        verify(userRepository).findById(userId);
        verify(mealRepository, never()).findById(any(UUID.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_ActiveBookingExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserUserIdAndStatus(userId, Status.Active)).thenReturn(Optional.of(booking));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookingService.createBooking(booking);
        });

        verify(userRepository).findById(userId);
        verify(bookingRepository).findByUserUserIdAndStatus(userId, Status.Active);
        verify(mealRepository, never()).findById(any(UUID.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testGetBookingById_Success() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.getBookedId());
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getBookingById(bookingId);
        });

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void testGetBookingByUserId_Success() {
        when(bookingRepository.findByUserUserIdAndStatus(userId, Status.Active)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getUserId());
        verify(bookingRepository).findByUserUserIdAndStatus(userId, Status.Active);
    }

    @Test
    void testGetBookingByUserId_NotFound() {
        when(bookingRepository.findByUserUserIdAndStatus(userId, Status.Active)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getBookingByUserId(userId);
        });

        verify(bookingRepository).findByUserUserIdAndStatus(userId, Status.Active);
    }

    @Test
    void testCancelBookingById_Success() {
        booking.setStatus(Status.Active);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.cancelBookingById(bookingId);

        assertNotNull(result);
        assertEquals(Status.Cancelled, result.getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCancelBookingById_NotFound() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.cancelBookingById(bookingId);
        });

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCancelBookingById_InvalidState() {
        booking.setStatus(Status.Cancelled);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(InvalidDataException.class, () -> {
            bookingService.cancelBookingById(bookingId);
        });

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testMarkBookingAsPickedUp_Success() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.markBookingAsPickedUp(bookingId, booking);

        assertNotNull(result);
        assertEquals(Status.PickedUp, result.getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testMarkBookingAsPickedUp_NotFound() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.markBookingAsPickedUp(bookingId, booking);
        });

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testGetAllActiveBookings() {
        List<Booking> activeBookings = List.of(
            Booking.builder().bookedId(UUID.randomUUID()).status(Status.Active).build(),
            Booking.builder().bookedId(UUID.randomUUID()).status(Status.Active).build()
        );

        when(bookingRepository.findByStatus(Status.Active,sort)).thenReturn(activeBookings);

        List<Booking> result = bookingService.getAllActiveBookings();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository).findByStatus(Status.Active,sort);
    }

    @Test
    void testGetBookingsByRestaurantID_Success() {
        List<Booking> restaurantBookings = List.of(
            Booking.builder().bookedId(UUID.randomUUID()).meal(meal).build(),
            Booking.builder().bookedId(UUID.randomUUID()).meal(meal).build()
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId,sort)).thenReturn(restaurantBookings);

        List<Booking> result = bookingService.getBookingsByRestaurantID(restaurantId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restaurantRepository).findById(restaurantId);
        verify(bookingRepository).findBookingByMeal_RestaurantRestaurantId(restaurantId,sort);
    }
    @Test
    void testGetBookingsByRestaurantID_RestaurantNotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getBookingsByRestaurantID(restaurantId);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(bookingRepository, never()).findBookingByMeal_RestaurantRestaurantId(any(UUID.class), any(Sort.class));
    }

    @Test
    void testGetActiveBookingsByRestaurantID_Success() {
        Booking activeBooking = Booking.builder().bookedId(UUID.randomUUID()).status(Status.Active).build();
        Booking inactiveBooking = Booking.builder().bookedId(UUID.randomUUID()).status(Status.Cancelled).build();
        List<Booking> restaurantBookings = List.of(activeBooking, inactiveBooking);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId,sort)).thenReturn(restaurantBookings);

        List<Booking> activeBookings = bookingService.getActiveBookingsByRestaurantID(restaurantId, true);
        List<Booking> inactiveBookings = bookingService.getActiveBookingsByRestaurantID(restaurantId, false);

        assertNotNull(activeBookings);
        assertEquals(1, activeBookings.size());
        assertEquals(Status.Active, activeBookings.get(0).getStatus());

        assertNotNull(inactiveBookings);
        assertEquals(1, inactiveBookings.size());
        assertNotEquals(Status.Active, inactiveBookings.get(0).getStatus());

        verify(restaurantRepository, times(2)).findById(restaurantId);
        verify(bookingRepository, times(2)).findBookingByMeal_RestaurantRestaurantId(restaurantId,sort);
    }

    @Test
    void testGetActiveBookingsByRestaurantID_RestaurantNotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            bookingService.getActiveBookingsByRestaurantID(restaurantId, true);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(bookingRepository, never()).findBookingByMeal_RestaurantRestaurantId(any(UUID.class),any(Sort.class));
    }
    @Test
    void testCreateBooking_MealAlreadyBooked() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserUserIdAndStatus(userId, Status.Active)).thenReturn(Optional.empty());
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(bookingRepository.findByMealMealIdAndStatus(mealId, Status.Active)).thenReturn(Optional.of(booking));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("This meal is already booked or picked up", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(bookingRepository).findByUserUserIdAndStatus(userId, Status.Active);
        verify(mealRepository).findById(mealId);
        verify(bookingRepository).findByMealMealIdAndStatus(mealId, Status.Active);
    }

    @Test
    void testGetAllUserBookings_Success() {
        List<Booking> userBookings = List.of(
            Booking.builder().bookedId(UUID.randomUUID()).user(user).build(),
            Booking.builder().bookedId(UUID.randomUUID()).user(user).build()
        );

        when(bookingRepository.findAllByUserUserId(userId,sort)).thenReturn(userBookings);

        List<Booking> result = bookingService.getAllUserBookings(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository).findAllByUserUserId(userId,sort);
    }
}

