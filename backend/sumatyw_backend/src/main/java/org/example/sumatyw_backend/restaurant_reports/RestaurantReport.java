package org.example.sumatyw_backend.user_reports;

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
public class RestaurantReport {
    @Id
    @GeneratedValue
    private UUID restaurantReportId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String cause;
    private String timestamp;
    private boolean isOpen;

}
