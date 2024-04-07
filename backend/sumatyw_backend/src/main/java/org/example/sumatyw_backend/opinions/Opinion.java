package org.example.sumatyw_backend.opinions;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.users.User;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID opinionId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private boolean isPositive;
    private String timestamp;
}
