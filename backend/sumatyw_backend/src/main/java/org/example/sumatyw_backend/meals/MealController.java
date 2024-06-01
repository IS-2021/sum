package org.example.sumatyw_backend.meals;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/meals")
public class MealController {

    private MealService mealService;

    @PostMapping()
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<MealDTO> addMeal(@RequestBody MealInputDTO mealInputDTO) {
        Meal meal = mealService.addMeal(MealDTOMapper.mapMealInputDTOToMeal(mealInputDTO));

        return new ResponseEntity<>(
            MealDTOMapper.mapMealToMealDTO(meal),
            HttpStatus.OK
        );
    }

    @GetMapping(params = {"restaurantId"})
    @PreAuthorize("hasAnyRole('RESTAURANT', 'USER')")
    public ResponseEntity<List<MealDTO>> getMealsByRestaurantId(@RequestParam("restaurantId") UUID restaurantId) {
        List<Meal> meals = mealService.getAllMealsByRestaurantId(restaurantId);

        return new ResponseEntity<>(
            meals.stream().map(MealDTOMapper::mapMealToMealDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT', 'USER')")
    public ResponseEntity<MealDTO> getMealById(@PathVariable("id") UUID id) {

        Meal meal = mealService.getMealById(id);
        return new ResponseEntity<>(MealDTOMapper.mapMealToMealDTO(meal),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<MealDTO> updateMealById(@PathVariable("id") UUID id, @RequestBody MealInputDTO mealInputDTO) {
        Meal meal = mealService.updateMealById(id, MealDTOMapper.mapMealInputDTOToMeal(mealInputDTO));

        return new ResponseEntity<>(
            MealDTOMapper.mapMealToMealDTO(meal),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<Void> deleteMealById(@PathVariable("id") UUID id) {
        mealService.removeMeal(id);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }
}
