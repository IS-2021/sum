package org.example.sumatyw_backend.bookings;


import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.users.User;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="booked_meals")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookedId;

    private LocalDateTime timestamp;

    private LocalDateTime pickedUpTimestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;
}
