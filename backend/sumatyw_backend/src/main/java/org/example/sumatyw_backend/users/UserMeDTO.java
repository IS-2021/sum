package org.example.sumatyw_backend.users;

import org.example.sumatyw_backend.addresses.AddressDTO;

import java.util.Optional;

public record UserMeDTO(
    String id,
    String firstName,
    String secondName,
    String username,
    String email,
    String phoneNumber,
    Role role,
    Optional<AddressDTO> address
) {
}
