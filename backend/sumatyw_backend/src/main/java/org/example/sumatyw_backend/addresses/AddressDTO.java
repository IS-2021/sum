package org.example.sumatyw_backend.addresses;

import java.util.UUID;

public record AddressDTO(

    UUID addressId,
    String number,
    String street,
    String postalCode,
    String city,
    double latitude,
    double longitude
) {
}
