package org.example.sumatyw_backend.bookings;

import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.users.User;

public class BookingDTOMapper {

    public static Booking mapBookingInputDTOToBooking(BookingInputDTO bookingInputDTO) {
        return Booking.builder()
            .meal(Meal.builder().mealId(bookingInputDTO.mealId()).build())
            .user(User.builder().userId(bookingInputDTO.userId()).build())
            .build();
    }

    public static BookingDTO mapBookingToBookingDTO(Booking booking) {
        return new BookingDTO(
            booking.getBookedId(),
            booking.getUser().getUserId(),
            booking.getMeal().getMealId(),
            booking.getTimestamp(),
            booking.getTimestamp().plusHours(1),
            booking.getPickedUpTimestamp() == null ? null : booking.getPickedUpTimestamp(),
            booking.getStatus()
        );
    }
}
