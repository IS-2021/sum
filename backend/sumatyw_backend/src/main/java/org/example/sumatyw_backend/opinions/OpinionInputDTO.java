package org.example.sumatyw_backend.opinions;

import java.util.UUID;

public record OpinionInputDTO(
    boolean isPositive,
    UUID userId,
    UUID restaurantId
) {
}
