package org.example.sumatyw_backend.meals;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.ingredients.Ingredient;
import org.example.sumatyw_backend.restaurants.Restaurant;

import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mealId;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @ManyToMany
    @JoinTable(
        name = "meal_ingredient",
        joinColumns = @JoinColumn(name = "meal_id", referencedColumnName = "mealId"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredientId")
    )
    private List<Ingredient> ingredients;

}
