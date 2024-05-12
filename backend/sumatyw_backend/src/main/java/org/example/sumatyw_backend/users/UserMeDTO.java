package org.example.sumatyw_backend.users;

import org.example.sumatyw_backend.cities.CityDTO;

public record UserMeDTO(
    String id,
    String firstName,
    String secondName,
    String username,
    String email,
    String phoneNumber,
    Role role,
    CityDTO city
) {
}
