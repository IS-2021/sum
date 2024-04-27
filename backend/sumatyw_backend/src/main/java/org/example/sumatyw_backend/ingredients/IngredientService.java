package org.example.sumatyw_backend.ingredients;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IngredientService {
    IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getIngredientById(UUID id) {
        return ingredientRepository.findById(id);
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
    }

    public Ingredient updateIngredientById(UUID id, Ingredient ingredient) {
        Ingredient existingIngredient = ingredientRepository.findById(id).orElseThrow(RuntimeException::new);
        existingIngredient.setName(ingredient.getName());
        existingIngredient.setType(ingredient.getType());
        return ingredientRepository.save(existingIngredient);
    }

}
