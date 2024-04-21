package org.example.sumatyw_backend.restaurants;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.opinions.Opinion;
import org.example.sumatyw_backend.users.User;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID restaurantId;
    private String name;
    private String phoneNumber;
    @OneToOne(mappedBy = "restaurant")
    private User user;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "restaurant")
    private List<Opinion> opinions;

    private boolean isActive;
}
