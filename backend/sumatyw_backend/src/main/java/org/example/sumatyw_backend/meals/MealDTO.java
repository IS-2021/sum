package org.example.sumatyw_backend.meals;

import java.util.UUID;

public record MealDTO (
    UUID mealId,
    UUID restaurantId,

    String name,

    String description
) {
}
