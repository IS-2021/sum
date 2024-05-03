package org.example.sumatyw_backend.favourites;

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
@Table(name="favourite")
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID favouriteId;
    private int orderNumber;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

