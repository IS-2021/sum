package org.example.sumatyw_backend.meals;


import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.users.User;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="booked_meals")
public class BookedMeals {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookedId;

    private String timestamp;

    private String picked_up_timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;
}
