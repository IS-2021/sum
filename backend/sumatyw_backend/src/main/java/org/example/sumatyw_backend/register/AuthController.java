package org.example.sumatyw_backend.register;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.users.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDTO> handleRegister(@Valid @RequestBody UserInputDTO userInputDTO) {
        User user = userService.addUser(UserDTOMapper.mapUserInputDTOToUser(userInputDTO));

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.CREATED
        );
    }


    @PreAuthorize("hasAnyRole('RESTAURANT','USER')")
    @PutMapping("/{id}/change-password")
    private ResponseEntity<Void> changePassword(@PathVariable("id") UUID id, @RequestBody PasswordDTO passwordDTO) {
        userService.changePassword(id, passwordDTO.getPassword());

        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }
}
