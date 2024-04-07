package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.restaurants.Restaurant;

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
            meal.getDescription()
        );
    }
}
