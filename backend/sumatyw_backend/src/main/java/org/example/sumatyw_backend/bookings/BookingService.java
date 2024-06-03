package org.example.sumatyw_backend.bookings;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.InvalidDataException;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.restaurants.RestaurantStatus;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.data.domain.Sort;
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

    private final Sort sort = Sort.by(Sort.Order.desc("timestamp"));

    public Booking createBooking(Booking booking) {
        userRepository.findById(booking.getUser().getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + booking.getUser().getUserId()));

        Optional<Booking> bookingDB = bookingRepository.findByUserUserIdAndStatus(booking.getUser().getUserId(), Status.Active);
        if (bookingDB.isPresent())
            throw  new ResourceAlreadyExistsException("User already have active booking");

        Meal mealDB = mealRepository.findById(booking.getMeal().getMealId())
            .orElseThrow(() -> new ObjectNotFoundException("Meal not found with ID: " + booking.getMeal().getMealId()));

        Restaurant restaurantDB = restaurantRepository.findById(mealDB.getRestaurant().getRestaurantId())
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + mealDB.getRestaurant().getRestaurantId()));

        if (restaurantDB.getStatus() != RestaurantStatus.Active)
            throw new InvalidDataException("Booking can be made in Active restaurant");

        Optional<Booking> activeMealBooking = bookingRepository.findByMealMealIdAndStatus(booking.getMeal().getMealId(), Status.Active);
        Optional<Booking> pickedUpMealBooking = bookingRepository.findByMealMealIdAndStatus(booking.getMeal().getMealId(), Status.PickedUp);
        if (activeMealBooking.isPresent() || pickedUpMealBooking.isPresent()) {
            throw  new ResourceAlreadyExistsException("This meal is already booked or picked up");
        }

        mealDB.setRestaurant(restaurantDB);
        booking.setMeal(mealDB);
        booking.setTimestamp(LocalDateTime.now());
        booking.setStatus(Status.Active);

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(UUID id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + id));
    }

    public Booking getBookingByUserId(UUID userId) {
        return bookingRepository.findByUserUserIdAndStatus(userId, Status.Active)
            .orElseThrow(() -> new ObjectNotFoundException("Active booking not found with user ID: " + userId));
    }

    public List<Booking> getAllUserBookings(UUID userId) {
        return bookingRepository.findAllByUserUserId(userId,sort);
    }

    public Booking cancelBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + id));

        if (booking.getStatus() != Status.Active)
            throw new InvalidDataException("Given booking is no longer in active state");

        booking.setStatus(Status.Cancelled);
        return bookingRepository.save(booking);
    }


    public List<Booking> getAllActiveBookings() {
        return bookingRepository.findByStatus(Status.Active,sort);
    }

    public List<Booking> getBookingsByRestaurantID(UUID restaurantId) {
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + restaurantId));
        return bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId,sort);
    }

    public List<Booking> getActiveBookingsByRestaurantID(UUID restaurantId, boolean active) {
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + restaurantId));
        List<Booking> bookings = bookingRepository.findBookingByMeal_RestaurantRestaurantId(restaurantId,sort);

        if (active) {
            return bookings.stream().filter(b -> b.getStatus() == Status.Active).toList();
        } else {
            return bookings.stream().filter(b -> b.getStatus() != Status.Active).toList();
        }
    }

    public Booking markBookingAsPickedUp(UUID bookingId, Booking booking) {
        Booking bookingDB = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + bookingId));

        if (bookingDB.getStatus() != Status.Active)
            throw new InvalidDataException("Given booking is no longer in active state");

        bookingDB.setPickedUpTimestamp(LocalDateTime.now());
        bookingDB.setStatus(Status.PickedUp);

        return bookingRepository.save(bookingDB);
    }
}
