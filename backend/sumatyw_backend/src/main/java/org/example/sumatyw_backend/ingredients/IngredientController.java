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
    public ResponseEntity<IngredientDTO> addOpinion(@RequestBody @Valid IngredientInputDTO ingredientInputDTO) {
        Ingredient ingredient = ingredientService.addIngredient(IngredientDTOMapper.mapIngredientInputDTOToIngredient(ingredientInputDTO));

        return new ResponseEntity<>(
            IngredientDTOMapper.mapIngredientToIngredientDTO(ingredient),
            HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<List<IngredientDTO>> getIngredients() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return new ResponseEntity<>(ingredients.stream().map(IngredientDTOMapper::mapIngredientToIngredientDTO).toList(), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> updateOpinion(@PathVariable("id") UUID id, @RequestBody @Valid IngredientInputDTO ingredientInputDTO) {
        Ingredient ingredient = ingredientService.updateIngredientById(id, IngredientDTOMapper.mapIngredientInputDTOToIngredient(ingredientInputDTO));

        return new ResponseEntity<>(
            IngredientDTOMapper.mapIngredientToIngredientDTO(ingredient),
            HttpStatus.OK
        );
    }

}
