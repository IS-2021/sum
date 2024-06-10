package org.example.sumatyw_backend.users;

import com.google.maps.errors.ApiException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();

        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") UUID id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'RESTAURANT')")
    public ResponseEntity<UserMeDTO> getMe() {
        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserMeDTO(userService.getMeUser()),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id) {
        userService.removeUserById(id);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable("id") UUID id, @Valid @RequestBody UserInputDTO updatedUser) {
        User user = userService.updateUserById(id, UserDTOMapper.mapUserInputDTOToUser(updatedUser));

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }

    @PostMapping("/{userId}/address")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserMeDTO> updateUserCity(@PathVariable("userId") UUID userId, @PathParam("placeId") String placeId) throws IOException, InterruptedException, ApiException {
        User user = userService.updateUserAddress(userId, placeId);

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserMeDTO(user),
            HttpStatus.OK
        );
    }
}
