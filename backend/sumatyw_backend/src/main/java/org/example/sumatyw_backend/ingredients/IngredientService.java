package org.example.sumatyw_backend.ingredients;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.bookings.Booking;
import org.example.sumatyw_backend.bookings.BookingRepository;
import org.example.sumatyw_backend.bookings.Status;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IngredientService {

    IngredientRepository ingredientRepository;
    MealRepository mealRepository;
    RestaurantRepository restaurantRepository;
    BookingRepository bookingRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(UUID id) {
        return ingredientRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Ingredient not found with ID: " + id));
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        Optional<Ingredient> ingredientDB = ingredientRepository.findByNameAndType(
            ingredient.getName(),
            ingredient.getType()
        );

        if (ingredientDB.isPresent()) {
            for (Meal m : ingredientDB.get().getMeals()) {
                if (m.getMealId() == ingredient.getMeals().get(0).getMealId())
                    throw new ResourceAlreadyExistsException("Given meal already has that meal");
            }
            ingredientDB.get().getMeals().add(ingredient.getMeals().get(0));
            return ingredientRepository.save(ingredientDB.get());
        } else {
            return ingredientRepository.save(ingredient);
        }

    }

    public List<Ingredient> getIngredientsByMealId(UUID mealId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(() -> new ObjectNotFoundException("Meal not found with ID: " + mealId));
        return new ArrayList<>(meal.getIngredients());
    }

    public List<Ingredient> getIngredientsByRestaurant(UUID restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + restaurantId));

        List<Meal> meals = restaurant.getMeals();
        List<Ingredient> ingredients = new ArrayList<>();

        for (Meal meal : meals) {
            List<Booking> bookingsDB = bookingRepository.findAllByMealMealId(meal.getMealId());

            if (bookingsDB.isEmpty()) {
                ingredients.addAll(meal.getIngredients());
            } else {
                for (Booking b : bookingsDB) {
                    if (b.getStatus() == Status.Cancelled || b.getStatus() == Status.OutOfDate) {
                        ingredients.addAll(meal.getIngredients());
                    }
                }
            }
        }

        return ingredients.stream().distinct().toList();
    }


    public Ingredient updateIngredientById(UUID id, Ingredient ingredient) {
        Ingredient existingIngredient = ingredientRepository.findById(id).orElseThrow(RuntimeException::new);
        existingIngredient.setName(ingredient.getName());
        existingIngredient.setType(ingredient.getType());
        return ingredientRepository.save(existingIngredient);
    }

}
