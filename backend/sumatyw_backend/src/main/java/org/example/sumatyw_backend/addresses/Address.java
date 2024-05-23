package org.example.sumatyw_backend.addresses;

import jakarta.persistence.*;
import lombok.*;
import org.example.sumatyw_backend.restaurants.Restaurant;

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
    @OneToOne(mappedBy = "address")
    private Restaurant restaurant;
    private double latitude;
    private double longitude;
}
