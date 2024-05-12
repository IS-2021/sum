package org.example.sumatyw_backend.bookings;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public Booking createBooking(Booking booking) {
        userRepository.findById(booking.getUser().getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + booking.getUser().getUserId()));

        mealRepository.findById(booking.getMeal().getMealId())
            .orElseThrow(() -> new ObjectNotFoundException("Meal not found with ID: " + booking.getMeal().getMealId()));

        booking.setTimestamp(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(UUID id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Booking not found with ID: " + id));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    


}
