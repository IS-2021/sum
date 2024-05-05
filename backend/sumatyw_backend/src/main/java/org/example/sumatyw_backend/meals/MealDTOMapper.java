package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.ingredients.IngredientDTO;
import org.example.sumatyw_backend.ingredients.IngredientDTOMapper;
import org.example.sumatyw_backend.restaurants.Restaurant;

import java.util.ArrayList;

public class MealDTOMapper {

    public static Meal mapMealInputDTOToMeal(MealInputDTO mealInputDTO) {
        return Meal.builder()
            .description(mealInputDTO.description())
            .name(mealInputDTO.name())
            .restaurant(Restaurant.builder().restaurantId(mealInputDTO.restaurantId()).build())
            .build();
    }

    public static MealDTO mapMealToMealDTO(Meal meal) {
        return new MealDTO(
            meal.getMealId(),
            meal.getRestaurant().getRestaurantId(),
            meal.getName(),
            meal.getDescription(),
            meal.getIngredients() == null ? new ArrayList<>() : meal.getIngredients().stream().map(IngredientDTOMapper::mapIngredientToIngredientDTO).toList()
        );
    }
}
