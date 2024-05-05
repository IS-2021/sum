package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.ingredients.IngredientDTO;

import java.util.List;
import java.util.UUID;

public record MealDTO (
    UUID mealId,
    UUID restaurantId,
    String name,
    String description,
    List<IngredientDTO> ingredients
) {
}
