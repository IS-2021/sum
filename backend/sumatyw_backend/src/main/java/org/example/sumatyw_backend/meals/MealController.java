package org.example.sumatyw_backend.meals;


import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/meals")
public class MealController {

    private MealService mealService;

    @PostMapping()
    public ResponseEntity<MealDTO> addMeal(@RequestBody MealInputDTO mealInputDTO) {
        Meal meal = mealService.addMeal(MealDTOMapper.mapMealInputDTOToMeal(mealInputDTO));

        return new ResponseEntity<>(
            MealDTOMapper.mapMealToMealDTO(meal),
            HttpStatus.OK
        );
    }

    @GetMapping(params = {"restaurantId"})
    public ResponseEntity<List<MealDTO>> getMealsByRestaurantId(@RequestParam("restaurantId") UUID restaurantId) {
        List<Meal> meals = mealService.getAllMealsByRestaurantId(restaurantId);

        return new ResponseEntity<>(
            meals.stream().map(MealDTOMapper::mapMealToMealDTO).toList(),
            HttpStatus.OK
        );
    }
}
