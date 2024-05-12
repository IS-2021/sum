//package org.example.sumatyw_backend.bookings;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.example.sumatyw_backend.meals.Meal;
//
//import java.util.UUID;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Setter
//@Getter
//@Entity
//@Builder
//@Table(name="meal_booked_meal")
//public class MealBookedMeal {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID mealBookedMealId;
//
//    @ManyToOne
//    @JoinColumn(name = "meal_id", nullable = false)
//    private Meal meal;
//
//    @ManyToOne
//    @JoinColumn(name = "booked_id", nullable = false)
//    private Booking bookedMeals;
//
//}
