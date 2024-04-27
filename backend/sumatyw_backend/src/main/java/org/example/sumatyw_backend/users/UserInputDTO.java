package org.example.sumatyw_backend.users;

import jakarta.validation.constraints.*;

public record UserInputDTO(

     @NotBlank(message = "User first name cannot be blank")
     @Size(max = 30, message = "User first name cannot contain more than 30 characters")
     String firstName,
     @NotBlank(message = "User second name cannot be blank")
     @Size(max = 30, message = "User second name cannot contain more than 30 characters")
     String secondName,
     @NotBlank(message = "User username cannot be blank")
     @Size(max = 30, message = "User username cannot contain more than 30 characters")
     String username,
     @NotBlank(message = "User email cannot be blank")
     @Email(message = "Invalid email pattern")
     @Size(max = 50, message = "User email cannot contain more than 50 characters")
     String email,
     @NotBlank(message = "User password cannot be blank")
     @Size(max = 30, message = "User password cannot contain more than 30 characters")
     @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
              message = "The user password is too weak")
     String password,
     @NotBlank(message = "User phone number cannot be blank")
     @Size(max = 10, message = "User phone number cannot contain more than 10 digits")
     String phoneNumber,
     @NotNull(message = "User role number cannot be blank")
//     @Pattern(regexp = "\\bROLE_USER\\b",
//              message = "User role must be 'USER_ROLE'")
     Role role
) {
}
