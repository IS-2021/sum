package org.example.sumatyw_backend.cities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CityInputDTO(
    @NotBlank(message = "City name cannot be blank")
    @Size(max = 50, message = "City name cannot contain more than 50 characters")
    String name,
    @NotBlank(message = "City country cannot be blank")
    @Size(max = 50, message = "City country cannot contain more than 50 characters")
    String country
) {
}
