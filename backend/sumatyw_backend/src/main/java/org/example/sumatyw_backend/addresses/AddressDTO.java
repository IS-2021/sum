package org.example.sumatyw_backend.addresses;

public record AddressDTO(
    String addressId,
    String number,
    String street,
    String postalCode,
    String city,
    double latitude,
    double longitude
) {
}
