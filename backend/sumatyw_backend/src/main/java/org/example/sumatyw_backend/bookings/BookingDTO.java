package org.example.sumatyw_backend.bookings;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDTO(

    UUID bookingId,
    UUID userId,
    UUID mealId,
    LocalDateTime orderedTimestamp,
    LocalDateTime deadlinePickUpTimestamp,
    LocalDateTime pickedUpTimestamp,
    Status status
) {
}
