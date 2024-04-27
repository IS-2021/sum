package org.example.sumatyw_backend.restaurants;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="restaurant_open")
public class RestaurantOpenHours {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID restaurant_openId;

}
