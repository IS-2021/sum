package org.example.sumatyw_backend.restaurants;

import org.example.sumatyw_backend.addresses.AddressDTO;

import java.util.UUID;

public record RestaurantDTO(
    UUID id,
    String name,
    String phoneNumber,
    UUID userId,
    AddressDTO address,
    Hours hours,
    String imageUrl,
    int likesCount,
    int dislikesCount,
    RestaurantStatus status
) {
}
