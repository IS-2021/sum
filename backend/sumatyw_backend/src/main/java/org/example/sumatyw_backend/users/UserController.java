package org.example.sumatyw_backend.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    public ResponseEntity getUserById(@PathVariable("id") UUID id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") UUID id) {
        if(userService.removeUserById(id)) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity updateUserById(@PathVariable("id") UUID id, @Valid @RequestBody UserInputDTO updatedUser) {
        User user = userService.updateUserById(id, UserDTOMapper.mapUserInputDTOToUser(updatedUser));

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }
}
