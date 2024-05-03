package org.example.sumatyw_backend.ingredients;

public class IngredientDTOMapper {
    public static Ingredient mapIngredientInputDTOToIngredient(IngredientInputDTO ingredientInputDTO) {
        return Ingredient.builder().name(ingredientInputDTO.name()).type(ingredientInputDTO.type()).build();
    }

    public static IngredientDTO mapIngredientToIngredientDTO(Ingredient ingredient) {
        return new IngredientDTO(ingredient.getIngredientId(), ingredient.getName(), ingredient.getType());
    }
}
