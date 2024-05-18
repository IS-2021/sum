package org.example.sumatyw_backend.addresses;

import org.example.sumatyw_backend.cities.CityDTO;

import java.util.UUID;

public record AddressDTO(

    UUID addressId,
    String number,
    String street,
    String postalCode,
    String city,
    String country,
    String region,
    double latitude,
    double longitude
) {
}
