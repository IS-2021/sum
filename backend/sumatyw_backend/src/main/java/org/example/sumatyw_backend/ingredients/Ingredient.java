package org.example.sumatyw_backend.ingredients;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.meals.Meal;

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

    @ManyToMany(mappedBy = "ingredients")
    private List<Meal> meals;
}
