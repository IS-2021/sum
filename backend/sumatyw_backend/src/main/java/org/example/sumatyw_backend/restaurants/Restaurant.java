package org.example.sumatyw_backend.restaurants;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.favourites.Favourite;
import org.example.sumatyw_backend.meals.Meal;
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
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "restaurant")
    private List<Opinion> opinions;
    @OneToMany(mappedBy = "restaurant")
    private List<Meal> meals;
    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Favourite> favourites;
    private String hours;
    private String imageUUID;
    private boolean active;
    private boolean banned;
    private int likesCount;
    private int dislikesCount;
}
