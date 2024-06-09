package org.example.sumatyw_backend.ingredients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class IngredientControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testAddIngredient() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();
        IngredientInputDTO ingredientInputDTO = new IngredientInputDTO("Tomato", "Vegetable");
        Ingredient ingredient = IngredientDTOMapper.mapIngredientInputDTOToIngredient(ingredientInputDTO, mealId);
        ingredient.setIngredientId(UUID.randomUUID());

        when(ingredientService.addIngredient(any(Ingredient.class))).thenReturn(ingredient);

        // when
        mockMvc.perform(post("/ingredients")
                .param("mealId", mealId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientInputDTO)))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.ingredientId").value(ingredient.getIngredientId().toString()));

        verify(ingredientService, times(1)).addIngredient(any(Ingredient.class));
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testGetIngredientsByMealIdAsRestaurant() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(UUID.randomUUID());
        List<Ingredient> ingredients = Arrays.asList(ingredient);

        when(ingredientService.getIngredientsByMealId(mealId)).thenReturn(ingredients);

        // when
        mockMvc.perform(get("/ingredients")
                .param("mealId", mealId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(ingredients.size()));

        verify(ingredientService, times(1)).getIngredientsByMealId(mealId);
    }

    @Test
    @WithMockUser()
    public void testGetIngredientsByMealIdAsUser() throws Exception {
        // given
        UUID mealId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(UUID.randomUUID());
        List<Ingredient> ingredients = Arrays.asList(ingredient);

        when(ingredientService.getIngredientsByMealId(mealId)).thenReturn(ingredients);

        // when
        mockMvc.perform(get("/ingredients")
                .param("mealId", mealId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(ingredients.size()));

        verify(ingredientService, times(1)).getIngredientsByMealId(mealId);
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testGetIngredientsByRestaurantIdAsRestaurant() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(UUID.randomUUID());
        List<Ingredient> ingredients = Arrays.asList(ingredient);

        when(ingredientService.getIngredientsByRestaurant(restaurantId)).thenReturn(ingredients);

        // when
        mockMvc.perform(get("/ingredients")
                .param("restaurantId", restaurantId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(ingredients.size()));

        verify(ingredientService, times(1)).getIngredientsByRestaurant(restaurantId);
    }

    @Test
    @WithMockUser()
    public void testGetIngredientsByRestaurantIdAsUser() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(UUID.randomUUID());
        List<Ingredient> ingredients = Arrays.asList(ingredient);

        when(ingredientService.getIngredientsByRestaurant(restaurantId)).thenReturn(ingredients);

        // when
        mockMvc.perform(get("/ingredients")
                .param("restaurantId", restaurantId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(ingredients.size()));

        verify(ingredientService, times(1)).getIngredientsByRestaurant(restaurantId);
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testGetIngredientByIdAsRestaurant() throws Exception {
        // given
        UUID ingredientId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientId);

        when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);

        // when
        mockMvc.perform(get("/ingredients/{id}", ingredientId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.ingredientId").value(ingredientId.toString()));

        verify(ingredientService, times(1)).getIngredientById(ingredientId);
    }

    @Test
    @WithMockUser()
    public void testGetIngredientByIdAsUser() throws Exception {
        // given
        UUID ingredientId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientId);

        when(ingredientService.getIngredientById(ingredientId)).thenReturn(ingredient);

        // when
        mockMvc.perform(get("/ingredients/{id}", ingredientId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.ingredientId").value(ingredientId.toString()));

        verify(ingredientService, times(1)).getIngredientById(ingredientId);
    }

    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testUpdateIngredient() throws Exception {
        // given
        UUID ingredientId = UUID.randomUUID();
        IngredientInputDTO ingredientInputDTO = new IngredientInputDTO("Tomato", "Vegetable");
        Ingredient ingredient = IngredientDTOMapper.mapIngredientInputDTOToIngredient(ingredientInputDTO);
        ingredient.setIngredientId(ingredientId);

        when(ingredientService.updateIngredientById(eq(ingredientId), any(Ingredient.class))).thenReturn(ingredient);

        // when
        mockMvc.perform(put("/ingredients/{id}", ingredientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientInputDTO)))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.ingredientId").value(ingredientId.toString()));

        verify(ingredientService, times(1)).updateIngredientById(eq(ingredientId), any(Ingredient.class));
    }
    @Test
    @WithMockUser(roles = {"RESTAURANT"})
    public void testGetAllIngredients() throws Exception {
        // given
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setIngredientId(UUID.randomUUID());
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setIngredientId(UUID.randomUUID());
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        // when
        mockMvc.perform(get("/ingredients"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(ingredients.size()))
            .andExpect(jsonPath("$[0].ingredientId").value(ingredient1.getIngredientId().toString()))
            .andExpect(jsonPath("$[1].ingredientId").value(ingredient2.getIngredientId().toString()));

        verify(ingredientService, times(1)).getAllIngredients();
    }


}
