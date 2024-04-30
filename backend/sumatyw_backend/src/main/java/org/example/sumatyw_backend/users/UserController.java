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
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") UUID id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
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

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable("id") UUID id, @Valid @RequestBody UserInputDTO updatedUser) {
        User user = userService.updateUserById(id, UserDTOMapper.mapUserInputDTOToUser(updatedUser));

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.OK
        );
    }

//    @PostMapping("/{id}/favourites")
//    public ResponseEntity<Void> addFavouriteRestaurantByUserId(@PathVariable("id") UUID userId, @RequestBody UUID restaurantId) {
//      //  userService.addFavouriteRestaurantByUserId(userId, restaurantId);
//
//        return new ResponseEntity<>(
//            HttpStatus.OK
//        );
//    }
//
//    @GetMapping("/{id}/favourites")
//    public ResponseEntity<List<RestaurantDTO>> getFavouriteRestaurantsByUserId(@PathVariable("id") UUID id) {
//        List<Restaurant> restaurants = userService.getFavouriteRestaurantsByUserId(id);
//
//        return new ResponseEntity<>(
//            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
//            HttpStatus.OK
//        );
//    }

//    @PutMapping("{id}/favourites")
//    public ResponseEntity<Void> updateFavouriteRestaurantsOrderByUserId(@PathVariable("id") UUID id, @RequestBody List<RestaurantFavouriteInputDTO> favourites) {
//
//    }

//    @DeleteMapping("/delete/favourites")
//    public ResponseEntity<Void> deleteFavouriteRestaurantsByUserId(@RequestBody DeleteFavouriteRestaurantsDTO deleteFavouriteRestaurantsDTO) {
//        userService.removeFavouriteRestaurantsByUserId(deleteFavouriteRestaurantsDTO);
//
//        return new ResponseEntity<>(
//            HttpStatus.NO_CONTENT
//        );
//    }
}
