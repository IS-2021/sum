package org.example.sumatyw_backend.meals;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.bookings.Booking;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MealService {

    private MealRepository mealRepository;
    private RestaurantRepository restaurantRepository;

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
            boolean hasActiveBooking = false;
            for (Booking booking : meals.get(i).getBookings()) {
                if (booking.isActive() || booking.getPickedUpTimestamp() != null)
                    hasActiveBooking = true;
            }

            if (hasActiveBooking) {
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

    public void removeMeal(UUID id) {
        mealRepository.deleteById(id);
    }

}
