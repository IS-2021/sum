package org.example.sumatyw_backend.meals;


import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.ingredients.Ingredient;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="meal_ingredient")
public class MealIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mealingredientId;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
}
