package org.example.sumatyw_backend.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.addresses.AddressInputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();

        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") UUID id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> getMe() {
        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserMeDTO(userService.getMeUser()),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id) {
        userService.removeUserById(id);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable("id") UUID id, @Valid @RequestBody UserInputDTO updatedUser) {
        User user = userService.updateUserById(id, UserDTOMapper.mapUserInputDTOToUser(updatedUser));

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }

    @PostMapping("/{userId}/city/{cityId}")
    public ResponseEntity<UserMeDTO> updateUserCity(@PathVariable("userId") UUID userId, @RequestBody AddressInputDTO addressInputDTO) {
        User user = userService.updateUserAddress(userId, addressInputDTO);

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserMeDTO(user),
            HttpStatus.OK
        );
    }
}
