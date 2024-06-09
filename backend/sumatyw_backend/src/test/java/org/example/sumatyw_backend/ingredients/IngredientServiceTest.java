package org.example.sumatyw_backend.ingredients;

import org.example.sumatyw_backend.bookings.Booking;
import org.example.sumatyw_backend.bookings.BookingRepository;
import org.example.sumatyw_backend.bookings.Status;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.meals.MealRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientService(ingredientRepository, mealRepository, restaurantRepository,bookingRepository);
    }

    @Test
    void getAllIngredients_ReturnsAllIngredients() {
        // given
        List<Ingredient> ingredients = new ArrayList<>();
        given(ingredientRepository.findAll()).willReturn(ingredients);

        // when
        List<Ingredient> result = ingredientService.getAllIngredients();

        // then
        verify(ingredientRepository).findAll();
        assertThat(result).isEqualTo(ingredients);
    }

    @Test
    void getIngredientById_ReturnsIngredient() {
        // given
        UUID ingredientId = UUID.randomUUID();
        Ingredient ingredient = Ingredient.builder().ingredientId(ingredientId).build();
        given(ingredientRepository.findById(ingredientId)).willReturn(Optional.of(ingredient));

        // when
        Ingredient result = ingredientService.getIngredientById(ingredientId);

        // then
        verify(ingredientRepository).findById(ingredientId);
        assertThat(result).isEqualTo(ingredient);
    }

    @Test
    void getIngredientById_ThrowsExceptionIfNotFound() {
        // given
        UUID ingredientId = UUID.randomUUID();
        given(ingredientRepository.findById(ingredientId)).willReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> ingredientService.getIngredientById(ingredientId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Ingredient not found with ID: " + ingredientId);
    }

    @Test
    void addIngredient_AddsNewIngredient() {
        // given
        Ingredient ingredient = Ingredient.builder().name("Tomato").type("Vegetable").meals(new ArrayList<>()).build();
        given(ingredientRepository.findByNameAndType("Tomato", "Vegetable")).willReturn(Optional.empty());
        given(ingredientRepository.save(ingredient)).willReturn(ingredient);

        // when
        Ingredient result = ingredientService.addIngredient(ingredient);

        // then
        verify(ingredientRepository).findByNameAndType("Tomato", "Vegetable");
        verify(ingredientRepository).save(ingredient);
        assertThat(result).isEqualTo(ingredient);
    }

    @Test
    void addIngredient_UpdatesExistingIngredient() {
        // given
        Meal meal = Meal.builder().mealId(UUID.randomUUID()).build();
        List<Meal> meals = new ArrayList<>();
        meals.add(meal);
        Ingredient existingIngredient = Ingredient.builder().ingredientId(UUID.randomUUID()).name("Tomato").type("Vegetable").meals(new ArrayList<>()).build();
        Ingredient ingredient = Ingredient.builder().name("Tomato").type("Vegetable").meals(meals).build();

        given(ingredientRepository.findByNameAndType("Tomato", "Vegetable")).willReturn(Optional.of(existingIngredient));
        given(ingredientRepository.save(existingIngredient)).willReturn(existingIngredient);

        // when
        Ingredient result = ingredientService.addIngredient(ingredient);

        // then
        verify(ingredientRepository).findByNameAndType("Tomato", "Vegetable");
        verify(ingredientRepository).save(existingIngredient);
        assertThat(result).isEqualTo(existingIngredient);
    }


    @Test
    void getIngredientsByMealId_ReturnsIngredients() {
        // given
        UUID mealId = UUID.randomUUID();
        Meal meal = Meal.builder().mealId(mealId).ingredients(new ArrayList<>()).build();
        given(mealRepository.findById(mealId)).willReturn(Optional.of(meal));

        // when
        List<Ingredient> result = ingredientService.getIngredientsByMealId(mealId);

        // then
        verify(mealRepository).findById(mealId);
        assertThat(result).isEqualTo(meal.getIngredients());
    }


    @Test
    void getIngredientsByMealId_ThrowsExceptionIfMealNotFound() {
        // given
        UUID mealId = UUID.randomUUID();
        given(mealRepository.findById(mealId)).willReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> ingredientService.getIngredientsByMealId(mealId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Meal not found with ID: " + mealId);
    }

    @Test
    void getIngredientsByRestaurant_ReturnsIngredients() {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.builder().restaurantId(restaurantId).meals(new ArrayList<>()).build();
        Meal meal1 = Meal.builder().mealId(UUID.randomUUID()).ingredients(new ArrayList<>()).build();
        Meal meal2 = Meal.builder().mealId(UUID.randomUUID()).ingredients(new ArrayList<>()).build();

        Ingredient ingredient = Ingredient.builder().
            ingredientId(UUID.randomUUID()).
            name("Tomato").
            type("Vegetable").
            build();

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setStatus(Status.OutOfDate);
        booking2.setStatus(Status.OutOfDate);
        meal1.setBookings(List.of(booking1));
        meal2.setBookings(List.of(booking2));
        meal1.getIngredients().add(ingredient);
        meal2.getIngredients().add(ingredient);
        restaurant.getMeals().add(meal1);
        restaurant.getMeals().add(meal2);

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        List<Ingredient> result = ingredientService.getIngredientsByRestaurant(restaurantId);

        // then
        verify(restaurantRepository).findById(restaurantId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertThat(result).containsExactlyInAnyOrder(ingredient);
    }
    @Test
    void updateIngredientById_ExistingIngredient_ReturnsUpdatedIngredient() {
        // Given
        UUID id = UUID.randomUUID();
        Ingredient existingIngredient = Ingredient.builder().
            ingredientId(UUID.randomUUID()).
            name("Apple").
            type("Fruit").
            build();
        Ingredient ingredient = Ingredient.builder().
            ingredientId(UUID.randomUUID()).
            name("Tomato").
            type("Vegetable").
            build();

        // Mocking repository behavior
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(existingIngredient));
        when(ingredientRepository.save(existingIngredient)).thenReturn(ingredient);

        // When
        Ingredient result = ingredientService.updateIngredientById(id, ingredient);

        // Then
        assertEquals(ingredient, result);
        assertEquals("Tomato", result.getName());
        assertEquals("Vegetable", result.getType());

        // Verify that findById and save methods were called
        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).save(existingIngredient);
    }

    @Test
    void updateIngredientById_NonExistingIngredient_ThrowsObjectNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        Ingredient updatedIngredient = Ingredient.builder().
            ingredientId(UUID.randomUUID()).
            name("Tomato").
            type("Vegetable").
            build();

        // Mocking repository behavior to return Optional.empty()
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // When and Then
        // Verify that trying to update a non-existing ingredient throws ObjectNotFoundException
        assertThrows(RuntimeException.class, () -> ingredientService.updateIngredientById(id, updatedIngredient));

        // Verify that findById was called
        verify(ingredientRepository).findById(id);
        // Verify that save was not called
        verify(ingredientRepository, never()).save(any());
    }
    @Test
    void getIngredientsByRestaurant_ThrowsExceptionIfRestaurantNotFound() {
        // given
        UUID restaurantId = UUID.randomUUID();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> ingredientService.getIngredientsByRestaurant(restaurantId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant not found with ID: " + restaurantId);
    }

    @Test
    void addIngredient_ThrowsResourceAlreadyExistsException() {
        // given
        UUID mealId = UUID.randomUUID();
        Meal meal = Meal.builder().mealId(mealId).build();
        List<Meal> meals = new ArrayList<>();
        meals.add(meal);

        Ingredient existingIngredient = Ingredient.builder().ingredientId(UUID.randomUUID()).name("Tomato").type("Vegetable").meals(meals).build();
        Ingredient newIngredient = Ingredient.builder().name("Tomato").type("Vegetable").meals(meals).build();

        given(ingredientRepository.findByNameAndType("Tomato", "Vegetable")).willReturn(Optional.of(existingIngredient));

        // when // then
        assertThatThrownBy(() -> ingredientService.addIngredient(newIngredient))
            .isInstanceOf(ResourceAlreadyExistsException.class)
            .hasMessageContaining("Given meal already has that ingredient");
    }

    @Test
    void getIngredientsByRestaurant_RemovesMealsWithActiveOrPickedUpBookings() {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.builder().restaurantId(restaurantId).meals(new ArrayList<>()).build();

        Meal meal1 = Meal.builder().mealId(UUID.randomUUID()).ingredients(new ArrayList<>()).build();
        Meal meal2 = Meal.builder().mealId(UUID.randomUUID()).ingredients(new ArrayList<>()).build();

        Ingredient ingredient1 = Ingredient.builder().ingredientId(UUID.randomUUID()).name("Tomato").type("Vegetable").build();
        Ingredient ingredient2 = Ingredient.builder().ingredientId(UUID.randomUUID()).name("Cucumber").type("Vegetable").build();

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setStatus(Status.Active);
        booking2.setStatus(Status.PickedUp);
        meal1.setBookings(List.of(booking1));
        meal2.setBookings(List.of(booking2));
        meal1.getIngredients().add(ingredient1);
        meal2.getIngredients().add(ingredient2);
        restaurant.getMeals().add(meal1);
        restaurant.getMeals().add(meal2);

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        List<Ingredient> result = ingredientService.getIngredientsByRestaurant(restaurantId);

        // then
        verify(restaurantRepository).findById(restaurantId);
        assertThat(result).isEmpty();
    }

    @Test
    void getIngredientsByRestaurant_ReturnsDistinctIngredients() {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.builder().restaurantId(restaurantId).meals(new ArrayList<>()).build();

        Meal meal1 = Meal.builder().mealId(UUID.randomUUID()).ingredients(new ArrayList<>()).build();
        Meal meal2 = Meal.builder().mealId(UUID.randomUUID()).ingredients(new ArrayList<>()).build();

        Ingredient ingredient = Ingredient.builder().ingredientId(UUID.randomUUID()).name("Tomato").type("Vegetable").build();
        Booking booking = new Booking();
        booking.setStatus(Status.OutOfDate);

        meal1.setBookings(List.of(booking));
        meal2.setBookings(List.of(booking));
        meal1.getIngredients().add(ingredient);
        meal2.getIngredients().add(ingredient);
        restaurant.getMeals().add(meal1);
        restaurant.getMeals().add(meal2);

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        List<Ingredient> result = ingredientService.getIngredientsByRestaurant(restaurantId);

        // then
        verify(restaurantRepository).findById(restaurantId);
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(ingredient);
    }

}
