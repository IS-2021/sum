//package org.example.sumatyw_backend.meals;
//
//import org.example.sumatyw_backend.restaurants.Restaurant;
//import org.example.sumatyw_backend.restaurants.RestaurantRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class MealServiceTest {
//
//    private MealService mealService;
//    @Mock
//    private MealRepository mealRepository;
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//
//    @BeforeEach
//    void setUp() {
//        mealService = new MealService(mealRepository, restaurantRepository);
//    }
//
//    @Test
//    void addMeal_Success() {
//        // given
//        UUID restaurantId = UUID.randomUUID();
//        Restaurant restaurant = new Restaurant();
//        restaurant.setRestaurantId(restaurantId);
//        Meal meal = Meal.builder()
//            .mealId(UUID.randomUUID())
//            .name("Test Meal")
//            .description("Test Description")
//            .restaurant(restaurant)
//            .build();
//
//        given(mealRepository.save(meal)).willReturn(meal);
//
//        // when
//        Meal savedMeal = mealService.addMeal(meal);
//
//        // then
//        ArgumentCaptor<Meal> mealCaptor = ArgumentCaptor.forClass(Meal.class);
//        verify(mealRepository).save(mealCaptor.capture());
//        Meal capturedMeal = mealCaptor.getValue();
//
//        assertThat(capturedMeal).isEqualTo(savedMeal);
//    }
//
//    @Test
//    void getMealById_Success() {
//        // given
//        UUID mealId = UUID.randomUUID();
//        Meal meal = Meal.builder().mealId(mealId).name("Test Meal").description("Test Description").build();
//
//        given(mealRepository.findById(mealId)).willReturn(Optional.of(meal));
//
//        // when
//        Meal foundMeal = mealService.getMealById(mealId);
//
//        // then
//        assertThat(foundMeal).isEqualTo(meal);
//    }
//
//    @Test
//    void getMealById_ThrowsException_WhenMealNotFound() {
//        // given
//        UUID mealId = UUID.randomUUID();
//        given(mealRepository.findById(mealId)).willReturn(Optional.empty());
//
//        // when / then
//        assertThatThrownBy(() -> mealService.getMealById(mealId))
//            .isInstanceOf(RuntimeException.class);
//    }
//
//    @Test
//    void getAllMealsByRestaurantId_ReturnsEmptyList_WhenNoActiveBookings() {
//        // given
//        UUID restaurantId = UUID.randomUUID();
//        List<Meal> meals = new ArrayList<>();
//        when(mealRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(meals);
//
//        // when
//        List<Meal> result = mealService.getAllMealsByRestaurantId(restaurantId);
//
//        // then
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void updateMealById_Success() {
//        // given
//        UUID mealId = UUID.randomUUID();
//        Meal existingMeal = Meal.builder().mealId(mealId).name("Old Name").description("Old Description").build();
//        Meal updateRequest = Meal.builder().name("New Name").description("New Description").build();
//
//        given(mealRepository.findById(mealId)).willReturn(Optional.of(existingMeal));
//        given(mealRepository.save(existingMeal)).willReturn(existingMeal);
//
//        // when
//        Meal updatedMeal = mealService.updateMealById(mealId, updateRequest);
//
//        // then
//        assertThat(existingMeal.getName()).isEqualTo(updateRequest.getName());
//        assertThat(existingMeal.getDescription()).isEqualTo(updateRequest.getDescription());
//        assertThat(updatedMeal).isEqualTo(existingMeal);
//    }
//
//    @Test
//    void updateMealById_ThrowsException_WhenMealNotFound() {
//        // given
//        UUID mealId = UUID.randomUUID();
//        Meal updateRequest = Meal.builder().name("New Name").description("New Description").build();
//
//        given(mealRepository.findById(mealId)).willReturn(Optional.empty());
//
//        // when / then
//        assertThatThrownBy(() -> mealService.updateMealById(mealId, updateRequest))
//            .isInstanceOf(RuntimeException.class);
//    }
//
//    @Test
//    void removeMeal_Success() {
//        // given
//        UUID mealId = UUID.randomUUID();
//
//        // when
//        mealService.removeMeal(mealId);
//
//        // then
//        verify(mealRepository).deleteById(mealId);
//    }
//}
