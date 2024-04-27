package org.example.sumatyw_backend.restaurants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantController {

    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody @Valid RestaurantInputDTO restaurantInputDTO) {
        Restaurant restaurant = restaurantService.addRestaurant(
            RestaurantDTOMapper.mapRestaurantInputDTOToRestaurant(restaurantInputDTO));

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable("id") UUID id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable("id") UUID id) {
        restaurantService.removeRestaurantById(id);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurantById(@PathVariable("id") UUID id, @RequestBody @Valid RestaurantInputDTO restaurantInputDTO) {
        Restaurant restaurant = restaurantService.updateRestaurantById(
            id, RestaurantDTOMapper.mapRestaurantInputDTOToRestaurant(restaurantInputDTO)
        );

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }
}
