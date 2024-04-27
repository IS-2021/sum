package org.example.sumatyw_backend.ingredients;

import java.util.UUID;

public record IngredientDTO(
    UUID ingredientId,
    String name,
    String type
) {
}
