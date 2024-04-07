package org.example.sumatyw_backend.meals;

import java.util.UUID;

public record MealInputDTO(
    UUID restaurantId,

    String name,

    String description
) {
}
