package org.example.sumatyw_backend.admin;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.restaurants.RestaurantDTO;
import org.example.sumatyw_backend.users.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }


    @GetMapping("/users/{id}")
    public ResponseEntity getUserByID(@PathVariable("id") UUID id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(
                UserDTOMapper.mapUserToUserDTO(user),
                HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {

        return null;
    }

}
