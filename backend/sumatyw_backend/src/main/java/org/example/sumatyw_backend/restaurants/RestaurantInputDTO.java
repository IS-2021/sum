package org.example.sumatyw_backend.restaurants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.sumatyw_backend.addresses.AddressInputDTO;

import java.util.UUID;

public record RestaurantInputDTO(
    @NotBlank(message = "Restaurant name cannot be blank")
    @Size(max = 50, message = "Restaurant name cannot contain more than 50 characters")
    String name,
    @NotBlank(message = "Restaurant phone number cannot be blank")
    @Size(max = 10, message = "Restaurant phone number cannot contain more than 10 characters")
    String phoneNumber,
    @NotNull(message = "Restaurant address cannot be blank")
    @Valid
    AddressInputDTO addressInputDTO,
    @NotNull(message = "Restaurant owner user id cannot be blank")
    UUID userId
) {
}
