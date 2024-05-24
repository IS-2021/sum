package org.example.sumatyw_backend.restaurants;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant addRestaurant(Restaurant restaurant) {

        restaurant.setRestaurantId(restaurant.getUser().getUserId());
        restaurant.setImageUUID("default");

        if (this.restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Restaurant with phone number: '" + restaurant.getPhoneNumber() + "' already exists.");

        return restaurantRepository.save(restaurant);
    }

    public RestaurantDTO banRestaurantById(UUID id) {

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.setBanned(true);

        return RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);

    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAllByActiveTrue();
    }

    public List<Restaurant> getRestaurantsByCity(String city) {
        return restaurantRepository.findAllByAddress_City_AndActiveTrue(city);
    }

    public RestaurantDTO deactivateRestaurant(UUID id) {

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Restaurant with id: " + id + " not found"));
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
        return RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);

    }

    public RestaurantDTO activateRestaurantById(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Restaurant with id: " + id + " not found"));
        restaurant.setActive(true);
        restaurantRepository.save(restaurant);
        return RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);
    }

    public Restaurant getRestaurantById(UUID id) {
        return restaurantRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + id));
    }

    public void removeRestaurantById(UUID id) {
        restaurantRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + id));

        restaurantRepository.deleteById(id);
    }

    public Restaurant updateRestaurantById(UUID id, Restaurant restaurant) {
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Restaurant not found with ID: " + id));

        if (!existingRestaurant.getPhoneNumber().equals(restaurant.getPhoneNumber())) {
            if (restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber()).isEmpty())
                existingRestaurant.setPhoneNumber(restaurant.getPhoneNumber());
        }

        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setHours(restaurant.getHours());

        return restaurantRepository.save(existingRestaurant);
    }

    public List<Restaurant> getAllPendingRestaurant() {

        return restaurantRepository.findAllByActiveFalse();
    }

    public void updateRestaurantImageUUID(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

}
