package org.example.sumatyw_backend.bookings;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookingInputDTO(
    @NotNull(message = "Meal ID can not be empty!")
    UUID mealId,
    @NotNull(message = "User ID can not be empty!")
    UUID userId
) {
}
