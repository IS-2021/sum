package org.example.sumatyw_backend.bookings;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDTO(
    UUID userId,
    UUID mealId,
    LocalDateTime timestamp,
    LocalDateTime pickedUpTimestamp
) {
}
