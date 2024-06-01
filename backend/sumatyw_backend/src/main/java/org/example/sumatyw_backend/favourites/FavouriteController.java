package org.example.sumatyw_backend.favourites;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantDTO;
import org.example.sumatyw_backend.restaurants.RestaurantDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;

    @PostMapping("/users/{id}/favourites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RestaurantDTO> addFavourite(@PathVariable("id") UUID userId, @RequestBody RestaurantFavouriteInputDTO favourite) {
        Restaurant restaurant = favouriteService.addFavourite(userId, favourite);

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @GetMapping("/users/{id}/favourites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RestaurantDTO>> getFavourites(@PathVariable("id") UUID userId) {
        List<Restaurant> restaurants = favouriteService.getFavouritesByUserId(userId);

        List<RestaurantDTO> restaurantDTOList = restaurants.stream()
            .map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO)
            .toList();

        return new ResponseEntity<>(
            restaurantDTOList,
            HttpStatus.OK
        );
    }
    
    @PutMapping("/users/{id}/favourites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateFavouritesOrder(@PathVariable("id") UUID userId, @RequestBody List<RestaurantFavouriteInputDTO> favourites) {
        favouriteService.updateFavourite(userId, favourites);

        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }

    @PostMapping("/users/delete/favourites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteFavourite(@RequestBody DeleteFavouriteRestaurantsDTO deleteFavouriteRestaurantsDTO) {
        favouriteService.deleteFavourite(deleteFavouriteRestaurantsDTO);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );

    }
}
