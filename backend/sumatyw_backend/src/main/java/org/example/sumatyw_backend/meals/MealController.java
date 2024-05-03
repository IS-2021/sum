package org.example.sumatyw_backend.meals;


import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/meals")
public class MealController {

    private MealService mealService;

    @PostMapping()
    public ResponseEntity addMeal(@RequestBody MealInputDTO mealInputDTO) {

        try {

            Meal meal = mealService.addMeal(MealDTOMapper.mapMealInputDTOToMeal(mealInputDTO));

            return new ResponseEntity<>(MealDTOMapper.mapMealToMealDTO(meal),
                HttpStatus.OK);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }

    }


}
