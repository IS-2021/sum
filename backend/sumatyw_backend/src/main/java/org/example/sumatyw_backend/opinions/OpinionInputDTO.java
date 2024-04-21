package org.example.sumatyw_backend.opinions;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OpinionInputDTO(

    boolean isPositive,
    @NotNull(message = "User id cannot be null")
    UUID userId,
    @NotNull(message = "Restaurant id cannot be null")
    UUID restaurantId
) {
}
