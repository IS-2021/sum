package org.example.sumatyw_backend.opinions;

import java.util.UUID;

public record OpinionDTO (
    UUID opinionId,
    boolean isPositive,
    UUID userId,
    UUID restaurantId
) {

}
