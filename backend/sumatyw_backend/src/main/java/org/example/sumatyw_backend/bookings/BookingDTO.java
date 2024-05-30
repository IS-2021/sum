package org.example.sumatyw_backend.bookings;

import org.example.sumatyw_backend.meals.MealDTO;
import org.example.sumatyw_backend.restaurants.RestaurantDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDTO(

    UUID bookingId,
    UUID userId,
    LocalDateTime orderedTimestamp,
    LocalDateTime deadlinePickUpTimestamp,
    LocalDateTime pickedUpTimestamp,
    Status status,
    MealDTO meal,
    RestaurantDTO restaurant
) {
}
