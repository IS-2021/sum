package org.example.sumatyw_backend.cities;

import java.util.UUID;

public record CityDTO(
    UUID cityId,
    String name,
    String region,
    String country
) {
}
