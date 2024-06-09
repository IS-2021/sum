package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.bookings.Booking;
import org.example.sumatyw_backend.bookings.BookingRepository;
import org.example.sumatyw_backend.bookings.Status;
import org.example.sumatyw_backend.exceptions.InvalidDataException;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.ingredients.Ingredient;
import org.example.sumatyw_backend.ingredients.IngredientRepository;
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

    @Mock
    private IngredientRepository ingredientRepository;

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
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(mealRepository.save(meal)).thenReturn(meal);
        mealService.addMeal(meal);
        verify(mealRepository, times(1)).save(meal);
    }

    @Test
    public void testAddMealRestaurantNotFound() {
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        meal.setRestaurant(restaurant);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> mealService.addMeal(meal));
        assertEquals("Restaurant with id " + restaurantId + " not found", exception.getMessage());
    }

    @Test
    public void testGetMealById() {
        UUID mealId = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setMealId(mealId);
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        Meal result = mealService.getMealById(mealId);
        assertEquals(mealId, result.getMealId());
    }

    @Test
    public void testGetMealByIdNotFound() {
        UUID mealId = UUID.randomUUID();
        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> mealService.getMealById(mealId));
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
    public void testGetAllMealsByRestaurantIdWithActiveBookings() {
        UUID restaurantId = UUID.randomUUID();
        Meal meal1 = new Meal();
        Meal meal2 = new Meal();
        meal1.setMealId(UUID.randomUUID());
        meal2.setMealId(UUID.randomUUID());
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setStatus(Status.Active);
        booking2.setStatus(Status.PickedUp);
        meal1.setBookings(List.of(booking1));
        meal2.setBookings(List.of(booking2));
        List<Meal> meals = new ArrayList<>();
        meals.add(meal1);
        meals.add(meal2);
        when(mealRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(meals);

        List<Meal> result = mealService.getAllMealsByRestaurantId(restaurantId);

        assertEquals(0, result.size());
    }

    @Test
    public void testRemoveMeal() {
        UUID mealId = UUID.randomUUID();

        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> mealService.removeMeal(mealId));
        assertEquals("Meal with ID: " + mealId + " not found", exception.getMessage());

        verify(mealRepository).findById(mealId);
        verifyNoInteractions(bookingRepository);
        verifyNoMoreInteractions(mealRepository);
    }

    @Test
    public void testRemoveMealWithActiveBooking() {
        UUID mealId = UUID.randomUUID();
        Meal mealDB = new Meal();
        mealDB.setMealId(mealId);
        Booking activeBooking = new Booking();
        activeBooking.setStatus(Status.Active);
        List<Booking> mealBookings = List.of(activeBooking);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(mealDB));
        when(bookingRepository.findAllByMealMealId(mealId)).thenReturn(mealBookings);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> mealService.removeMeal(mealId));
        assertEquals("This meal already have active booking or it is in booking picked up history", exception.getMessage());
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

        verify(mealRepository).findById(mealId);
        verify(mealRepository).save(any());
        assertEquals("Updated Meal", result.getName());
        assertEquals("New Description", result.getDescription());
    }
    @Test
    public void testRemoveMealWithNoActiveBooking() {
        UUID mealId = UUID.randomUUID();
        Meal mealDB = new Meal();
        mealDB.setMealId(mealId);
        List<Ingredient> ingredients = new ArrayList<>();
        List<Booking> mealBookings = new ArrayList<>();

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(mealDB));
        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(bookingRepository.findAllByMealMealId(mealId)).thenReturn(mealBookings);

        assertDoesNotThrow(() -> mealService.removeMeal(mealId));

        verify(mealRepository).findById(mealId);
        verify(ingredientRepository).findAll();
        verify(bookingRepository).findAllByMealMealId(mealId);
        verify(mealRepository).deleteById(mealId);
    }

    @Test
    public void testAddMeal_Success() {
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        meal.setRestaurant(restaurant);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(mealRepository.save(meal)).thenReturn(meal);

        Meal result = mealService.addMeal(meal);

        assertNotNull(result);
        verify(mealRepository, times(1)).save(meal);
    }

    @Test
    public void testGetMealById_ExistingMeal() {

        UUID mealId = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setMealId(mealId);

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));

        Meal result = mealService.getMealById(mealId);

        assertEquals(mealId, result.getMealId());
    }

    @Test
    public void testUpdateMealById_ExistingMeal() {
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

        verify(mealRepository).findById(mealId);
        verify(mealRepository).save(any());
        assertEquals("Updated Meal", result.getName());
        assertEquals("New Description", result.getDescription());
    }

    @Test
    public void testRemoveMeal_ExistingMeal() {
        UUID mealId = UUID.randomUUID();
        Meal mealDB = new Meal();
        mealDB.setMealId(mealId);
        List<Ingredient> ingredients = new ArrayList<>();
        List<Booking> mealBookings = new ArrayList<>();

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(mealDB));
        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(bookingRepository.findAllByMealMealId(mealId)).thenReturn(mealBookings);

        assertDoesNotThrow(() -> mealService.removeMeal(mealId));

        verify(mealRepository).findById(mealId);
        verify(ingredientRepository).findAll();
        verify(bookingRepository).findAllByMealMealId(mealId);
        verify(mealRepository).deleteById(mealId);
    }
    @Test
    public void testGetAllMealsByRestaurantId_RemoveMealsWithActiveBookings() {
        UUID restaurantId = UUID.randomUUID();
        Meal meal1 = new Meal();
        Meal meal2 = new Meal();
        meal1.setMealId(UUID.randomUUID());
        meal2.setMealId(UUID.randomUUID());
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setStatus(Status.Active);
        booking2.setStatus(Status.PickedUp);
        meal1.setBookings(List.of(booking1));
        meal2.setBookings(List.of(booking2));
        List<Meal> meals = new ArrayList<>();
        meals.add(meal1);
        meals.add(meal2);
        when(mealRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(meals);

        List<Meal> result = mealService.getAllMealsByRestaurantId(restaurantId);

        assertEquals(0, result.size());
        verify(mealRepository).findAllByRestaurantRestaurantId(restaurantId);
    }

    @Test
    public void testRemoveMeal_MealNotFound() {
        UUID mealId = UUID.randomUUID();

        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> mealService.removeMeal(mealId));
        assertEquals("Meal with ID: " + mealId + " not found", exception.getMessage());
    }

}
