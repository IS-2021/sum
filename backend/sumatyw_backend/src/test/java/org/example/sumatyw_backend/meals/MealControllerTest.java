package org.example.sumatyw_backend.meals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MealControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MealService mealService;

    @InjectMocks
    private MealController mealController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(mealController).build();
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testAddMeal() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        MealInputDTO mealInputDTO = new MealInputDTO(restaurantId, "Pasta", "Delicious pasta with cheese");
        Meal meal = MealDTOMapper.mapMealInputDTOToMeal(mealInputDTO);
        meal.setMealId(UUID.randomUUID());

        when(mealService.addMeal(any(Meal.class))).thenReturn(meal);

        // when
        mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealInputDTO)))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mealId").value(meal.getMealId().toString()));

        verify(mealService, times(1)).addMeal(any(Meal.class));
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testGetMealsByRestaurantIdAsRestaurant() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(UUID.randomUUID());
        meal.setMealId(UUID.randomUUID());
        meal.setRestaurant(restaurant);
        List<Meal> meals = Arrays.asList(meal);

        when(mealService.getAllMealsByRestaurantId(restaurantId)).thenReturn(meals);

        // when
        mockMvc.perform(get("/meals")
                .param("restaurantId", restaurantId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(meals.size()));

        verify(mealService, times(1)).getAllMealsByRestaurantId(restaurantId);
    }

    @Test
    @WithMockUser()
    public void testGetMealsByRestaurantIdAsUser() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(UUID.randomUUID());
        meal.setMealId(UUID.randomUUID());
        meal.setRestaurant(restaurant);
        List<Meal> meals = Arrays.asList(meal);

        when(mealService.getAllMealsByRestaurantId(restaurantId)).thenReturn(meals);

        // when
        mockMvc.perform(get("/meals")
                .param("restaurantId", restaurantId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(meals.size()));

        verify(mealService, times(1)).getAllMealsByRestaurantId(restaurantId);
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testGetMealByIdAsRestaurant() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setMealId(mealId);
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        meal.setRestaurant(restaurant);

        when(mealService.getMealById(mealId)).thenReturn(meal);

        // when
        mockMvc.perform(get("/meals/{id}", mealId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mealId").value(mealId.toString()))
            .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()));

        verify(mealService, times(1)).getMealById(mealId);
    }

    @Test
    @WithMockUser()
    public void testGetMealByIdAsUser() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setMealId(mealId);
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        meal.setRestaurant(restaurant);

        when(mealService.getMealById(mealId)).thenReturn(meal);

        // when
        mockMvc.perform(get("/meals/{id}", mealId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mealId").value(mealId.toString()))
            .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()));

        verify(mealService, times(1)).getMealById(mealId);
    }


    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testUpdateMealById() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();
        MealInputDTO mealInputDTO = new MealInputDTO(UUID.randomUUID(), "Updated Pasta", "Updated delicious pasta with cheese");
        Meal meal = MealDTOMapper.mapMealInputDTOToMeal(mealInputDTO);
        meal.setMealId(mealId);

        when(mealService.updateMealById(eq(mealId), any(Meal.class))).thenReturn(meal);

        // when
        mockMvc.perform(put("/meals/{id}", mealId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealInputDTO)))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mealId").value(mealId.toString()));

        verify(mealService, times(1)).updateMealById(eq(mealId), any(Meal.class));
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testDeleteMealById() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();

        // when
        mockMvc.perform(delete("/meals/{id}", mealId))
            // then
            .andExpect(status().isNoContent());

        verify(mealService, times(1)).removeMeal(mealId);
    }
}
