package org.example.sumatyw_backend.meals;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.bookings.Booking;
import org.example.sumatyw_backend.bookings.BookingRepository;
import org.example.sumatyw_backend.bookings.Status;
import org.example.sumatyw_backend.exceptions.InvalidDataException;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.ingredients.Ingredient;
import org.example.sumatyw_backend.ingredients.IngredientRepository;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MealService {

    private MealRepository mealRepository;
    private RestaurantRepository restaurantRepository;
    private BookingRepository bookingRepository;
    private IngredientRepository ingredientRepository;

    public Meal addMeal(Meal meal) {
        restaurantRepository.findById(meal.getRestaurant().getRestaurantId()).orElseThrow(() -> new ObjectNotFoundException("Restaurant with id " + meal.getRestaurant().getRestaurantId() + " not found" ));
        return mealRepository.save(meal);
    }

    public Meal getMealById(UUID id) {
        return mealRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Meal> getAllMealsByRestaurantId(UUID restaurantId) {
        List<Meal> meals = mealRepository.findAllByRestaurantRestaurantId(restaurantId);

        for (int i = 0; i < meals.size(); i++) {
            boolean hasBooking = false;
            for (Booking booking : meals.get(i).getBookings()) {
                if (booking.getStatus() == Status.Active || booking.getStatus() == Status.PickedUp)
                    hasBooking = true;
            }

            if (hasBooking) {
                meals.remove(i);
                i--;
            }
        }

        return meals;
    }

    public Meal updateMealById(UUID id, Meal meal) {
        Meal existingMeal = mealRepository.findById(id).orElseThrow(RuntimeException::new);
        existingMeal.setName(meal.getName());
        existingMeal.setDescription(meal.getDescription());
        return mealRepository.save(existingMeal);
    }

    @Transactional
    public void removeMeal(UUID id) {
        Meal mealDB = mealRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Meal with ID: " + id + " not found"));
        List<Ingredient> ingredients = ingredientRepository.findAll();

        List<Booking> mealBookings = bookingRepository.findAllByMealMealId(id);

        for (Booking b : mealBookings) {
            if (b.getStatus() == Status.Active || b.getStatus() == Status.PickedUp) {
                throw new InvalidDataException("This meal already have active booking or it is in booking picked up history");
            }
        }

        for (Ingredient i : ingredients) {
            i.getMeals().remove(mealDB);
            ingredientRepository.saveAll(ingredients);
        }

        bookingRepository.deleteAll(mealBookings);

        mealDB.setIngredients(new ArrayList<>());

        mealRepository.deleteById(id);
    }

}
