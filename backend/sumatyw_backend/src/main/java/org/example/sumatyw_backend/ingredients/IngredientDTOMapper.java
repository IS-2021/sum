package org.example.sumatyw_backend.ingredients;

import org.example.sumatyw_backend.meals.Meal;

import java.util.Arrays;
import java.util.UUID;

public class IngredientDTOMapper {
    public static Ingredient mapIngredientInputDTOToIngredient(IngredientInputDTO ingredientInputDTO, UUID mealId) {
        return Ingredient.builder()
            .name(ingredientInputDTO.name())
            .type(ingredientInputDTO.type())
            .meals(Arrays.asList(Meal.builder().mealId(mealId).build()))
            .build();
    }

    public static Ingredient mapIngredientInputDTOToIngredient(IngredientInputDTO ingredientInputDTO) {
        return Ingredient.builder()
            .name(ingredientInputDTO.name())
            .type(ingredientInputDTO.type())
            .build();
    }

    public static IngredientDTO mapIngredientToIngredientDTO(Ingredient ingredient) {
        return new IngredientDTO(
            ingredient.getIngredientId(),
            ingredient.getName(),
            ingredient.getType()
        );
    }
}
