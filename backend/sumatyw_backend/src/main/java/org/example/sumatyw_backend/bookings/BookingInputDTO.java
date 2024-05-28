package org.example.sumatyw_backend.bookings;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record BookingInputDTO(
    @NotBlank(message = "Meal ID can not be empty!")
    UUID mealId,
    @NotBlank(message = "User ID can not be empty!")
    UUID userId
) {
}
