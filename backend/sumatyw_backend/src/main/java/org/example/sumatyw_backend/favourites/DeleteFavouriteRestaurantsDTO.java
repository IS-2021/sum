package org.example.sumatyw_backend.favourites;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;
import java.util.List;

public record DeleteFavouriteRestaurantsDTO(

    @NotNull(message = "User id cannot be blank")
    UUID userId,
    List<UUID> restaurantIds
) {
}
