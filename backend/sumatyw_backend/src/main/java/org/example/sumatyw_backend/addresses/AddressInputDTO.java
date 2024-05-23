package org.example.sumatyw_backend.addresses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddressInputDTO(

    @NotBlank(message = "Address street number cannot be blank")
    @Size(max = 5, message = "Address street number cannot contain more than 5 characters")
    String number,
    @NotBlank(message = "Street name number cannot be blank")
    @Size(max = 50, message = "Street name cannot contain more than 50 characters")
    String street,
    @NotBlank(message = "Address postal code cannot be blank")
    @Size(max = 6, message = "City postal code cannot contain more than 6 characters")
    String postalCode,
    @NotNull(message = "City cannot be blank")
    String city,
    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country name cannot contain more than 50 characters")
    String country,
    double latitude,
    double longitude
) {
}
