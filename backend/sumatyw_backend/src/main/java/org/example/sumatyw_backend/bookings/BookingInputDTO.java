package org.example.sumatyw_backend.bookings;

import java.util.UUID;

public record BookingInputDTO(
    UUID mealId,
    UUID userId
) {
}
