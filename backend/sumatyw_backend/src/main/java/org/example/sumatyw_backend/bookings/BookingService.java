package org.example.sumatyw_backend.bookings;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public Booking createBooking(Booking booking) {
        userRepository.findById(booking.getUser().getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + booking.getUser().getUserId()));

        Optional<Booking> bookingDB = bookingRepository.findByUserUserIdAndActiveIsTrue(booking.getUser().getUserId());
        if (bookingDB.isPresent())
            throw  new ResourceAlreadyExistsException("User already have active booking");

        mealRepository.findById(booking.getMeal().getMealId())
            .orElseThrow(() -> new ObjectNotFoundException("Meal not found with ID: " + booking.getMeal().getMealId()));

        Optional<Booking> mealBooking = bookingRepository.findByMealMealIdAndActiveIsTrue(booking.getMeal().getMealId());
        if (mealBooking.isPresent()) {
            throw  new ResourceAlreadyExistsException("This meal is already booked");
        }

        booking.setTimestamp(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(UUID id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + id));
    }

    public Booking getBookingByUserId(UUID userId) {
        return bookingRepository.findByUserUserIdAndActiveIsTrue(userId)
            .orElseThrow(() -> new ObjectNotFoundException("Active booking not found with user ID: " + userId));
    }

    public List<Booking> getAllUserBookings(UUID userId) {
        return bookingRepository.findAllByUserUserId(userId);
    }

    public void deleteBookingById(UUID id) {
        bookingRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + id));

        bookingRepository.deleteById(id);
    }


    public List<Booking> getAllActiveBookings() {
        return bookingRepository.findBookingByActiveIsTrue();
    }

    public List<Booking> getBookingsByRestaurantID(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + restaurantId));
        return bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId);
    }

    public List<Booking> getActiveBookingsByRestaurantID(UUID restaurantId, boolean active) {
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + restaurantId));

        List<Booking> bookings = bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId);

        return bookings.stream().filter(b -> b.isActive() == active).toList();
    }

    public Booking markBookingAsPickedUp(UUID bookingId, Booking booking) {
        Booking bookingDB = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + bookingId));

        bookingDB.setPickedUpTimestamp(LocalDateTime.now());
        bookingDB.setActive(false);

        return bookingRepository.save(bookingDB);
    }
}
