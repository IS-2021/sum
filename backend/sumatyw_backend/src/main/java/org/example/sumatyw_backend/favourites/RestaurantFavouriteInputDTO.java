package org.example.sumatyw_backend.favourites;

import java.util.UUID;

public record  RestaurantFavouriteInputDTO(
    UUID restaurantId,
    int orderNumber
) {
}
