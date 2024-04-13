package org.example.sumatyw_backend.meals;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MealService {

    private MealRepository mealRepository;

    public Meal addMeal(Meal meal) {

        return mealRepository.save(meal);
    }

    public Meal getMealById(UUID id) {
        return mealRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Meal> getAllMealsByRestaurant(Restaurant restaurant) {
        return mealRepository.findAllByRestaurant(restaurant);
    }

    public Meal updateMealById(UUID id, Meal meal) {
        Meal existingMeal = mealRepository.findById(id).orElseThrow(RuntimeException::new);
        existingMeal.setName(meal.getName());
        existingMeal.setDescription(meal.getDescription());
        return mealRepository.save(existingMeal);
    }

    public void removeMeal(Meal meal) {
        mealRepository.delete(meal);
    }

}
