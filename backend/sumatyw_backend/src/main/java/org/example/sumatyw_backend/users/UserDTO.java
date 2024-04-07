package org.example.sumatyw_backend.users;

public record UserDTO(
    String firstName,
    String secondName,
    String username,
    String email,
    String phoneNumber,
    Role role
) {
}
