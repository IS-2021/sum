package org.example.sumatyw_backend.meals;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="meal_booked_meal")
public class MealBookedMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mealBookedMealId;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "booked_id", nullable = false)
    private BookedMeals bookedMeals;

}
