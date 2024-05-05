package org.example.sumatyw_backend.ingredients;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.meals.Meal;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ingredientId;

    private String name;

    private String type;
    @ManyToMany()
    @JoinTable(
        name = "ingredient_meal",
        joinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredientId"),
        inverseJoinColumns = @JoinColumn(name = "meal_id", referencedColumnName = "mealId")
    )
    private List<Meal> meals;
}
