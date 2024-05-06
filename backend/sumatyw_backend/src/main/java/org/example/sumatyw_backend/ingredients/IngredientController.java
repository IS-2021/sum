package org.example.sumatyw_backend.ingredients;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
public class IngredientController {
    private IngredientService ingredientService;

    @PostMapping()
    public ResponseEntity<IngredientDTO> addIngredient(@RequestBody @Valid IngredientInputDTO ingredientInputDTO, @RequestParam("mealId") UUID mealId) {
        Ingredient ingredient = ingredientService.addIngredient(IngredientDTOMapper.mapIngredientInputDTOToIngredient(ingredientInputDTO, mealId));

        return new ResponseEntity<>(
            IngredientDTOMapper.mapIngredientToIngredientDTO(ingredient),
            HttpStatus.OK
        );
    }

    @GetMapping(params = {"mealId"})
    public ResponseEntity<List<IngredientDTO>> getIngredientsByMealId(@RequestParam("mealId") UUID mealId) {
        List<Ingredient> ingredientsByMeal = ingredientService.getIngredientsByMealId(mealId);
        return new ResponseEntity<>(ingredientsByMeal.stream().map(IngredientDTOMapper::mapIngredientToIngredientDTO).toList(), HttpStatus.OK);

    }

    @GetMapping(params = {"restaurantId"})
    public ResponseEntity<List<IngredientDTO>> getIngredientsByRestaurantId(@RequestParam("restaurantId") UUID restaurandId) {
        List<Ingredient> ingredients = ingredientService.getIngredientsByRestaurant(restaurandId);

        return new ResponseEntity<>(ingredients.stream().map(IngredientDTOMapper::mapIngredientToIngredientDTO).toList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable UUID id) {

        Ingredient ingredient = ingredientService.getIngredientById(id);
        return new ResponseEntity<>(IngredientDTOMapper.mapIngredientToIngredientDTO(ingredient), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(@PathVariable("id") UUID id, @RequestBody @Valid IngredientInputDTO ingredientInputDTO) {
        Ingredient ingredient = ingredientService.updateIngredientById(id, IngredientDTOMapper.mapIngredientInputDTOToIngredient(ingredientInputDTO));

        return new ResponseEntity<>(
            IngredientDTOMapper.mapIngredientToIngredientDTO(ingredient),
            HttpStatus.OK
        );
    }

}
