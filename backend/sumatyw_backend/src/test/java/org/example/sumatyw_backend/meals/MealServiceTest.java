package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.bookings.Booking;
import org.example.sumatyw_backend.bookings.BookingRepository;
import org.example.sumatyw_backend.bookings.Status;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private MealService mealService;

    @Test
    public void testAddMeal() {
        UUID mealId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setMealId(mealId);
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        meal.setRestaurant(restaurant);
        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(restaurant));
        when(mealRepository.save(meal)).thenReturn(meal);
        mealService.addMeal(meal);
        verify(mealRepository, times(1)).save(meal);
    }

    @Test
    public void testGetMealById() {
        UUID mealId = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setMealId(mealId);
        when(mealRepository.findById(mealId)).thenReturn(java.util.Optional.of(meal));
        Meal result = mealService.getMealById(mealId);
        assertEquals(mealId, result.getMealId());
    }

    @Test
    public void testGetAllMealsByRestaurantId() {
        UUID restaurantId = UUID.randomUUID();
        Meal meal1 = new Meal();
        Meal meal2 = new Meal();
        meal1.setMealId(UUID.randomUUID());
        meal2.setMealId(UUID.randomUUID());
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setStatus(Status.OutOfDate);
        booking2.setStatus(Status.OutOfDate);
        meal1.setBookings(List.of(booking1));
        meal2.setBookings(List.of(booking2));
        List<Meal> meals = new ArrayList<>();
        meals.add(meal1);
        meals.add(meal2);
        when(mealRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(meals);

        List<Meal> result = mealService.getAllMealsByRestaurantId(restaurantId);

        assertEquals(2, result.size());
        assertEquals(meal2.getMealId(), result.get(1).getMealId());
    }

    @Test
    public void testRemoveMeal() {
        // Create test data
        UUID mealId = UUID.randomUUID();

        // Mock behavior of repositories
        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());

        // Call the method to test and verify exception
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> mealService.removeMeal(mealId));
        assertEquals("Meal with ID: " + mealId + " not found", exception.getMessage());

        // Verify repository interactions
        verify(mealRepository).findById(mealId);
        verifyNoInteractions(bookingRepository);
        verifyNoMoreInteractions(mealRepository);
    }
    @Test
    public void testUpdateMealById() {
        UUID mealId = UUID.randomUUID();
        Meal existingMeal = new Meal();
        existingMeal.setMealId(mealId);
        existingMeal.setName("Existing Meal");
        existingMeal.setDescription("Old Description");
        Meal updatedMeal = new Meal();
        updatedMeal.setName("Updated Meal");
        updatedMeal.setDescription("New Description");

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(existingMeal));
        when(mealRepository.save(any())).then(returnsFirstArg());

        Meal result = mealService.updateMealById(mealId, updatedMeal);

        // Verify repository interactions
        verify(mealRepository).findById(mealId);
        verify(mealRepository).save(any());
        assertEquals("Updated Meal", result.getName());
        assertEquals("New Description", result.getDescription());
    }
}
