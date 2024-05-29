package org.example.sumatyw_backend.addresses;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.restaurants.Restaurant;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "addresses")
public class Address {
    /**
     * Address ID. Equals Place ID from Google Maps API.
     */
    @Id
    private String addressId;
    private String city;
    private String street;
    private String number;
    private String postalCode;
    private String country;
    @OneToMany(mappedBy = "address")
    private List<Restaurant> restaurants;
    private double latitude;
    private double longitude;
}
