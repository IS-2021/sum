package org.example.sumatyw_backend.restaurants;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressRepository;
import org.example.sumatyw_backend.cities.City;
import org.example.sumatyw_backend.cities.CityRepository;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    public Restaurant addRestaurant(Restaurant restaurant) {

        Optional<Address> addressDB = addressRepository.findByCity_NameAndStreetAndNumberAndPostalCode(
            restaurant.getAddress().getCity().getName(),
            restaurant.getAddress().getStreet(),
            restaurant.getAddress().getNumber(),
            restaurant.getAddress().getPostalCode()
        );

        Optional<City> city = cityRepository.findByName(restaurant.getAddress().getCity().getName());

        city.ifPresent(c -> restaurant.getAddress().setCity(c));

        if (addressDB.isPresent())
            throw new ResourceAlreadyExistsException("Restaurant with given address already exists");

        if (this.restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Restaurant with phone number: '" + restaurant.getPhoneNumber() + "' already exists.");

        return restaurantRepository.save(restaurant);
    }

    public RestaurantDTO banRestaurantById(UUID id) {

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.setActive(false);

        return RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);

    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
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

        if (restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Restaurant with phone number: '" + restaurant.getPhoneNumber() + "' already exists.");

        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setPhoneNumber(restaurant.getPhoneNumber());

        return restaurantRepository.save(existingRestaurant);
    }

    public List<Restaurant> getAllPendingRestaurant() {

        if(!restaurantRepository.findByIsActiveTrue().isEmpty()) {
            return restaurantRepository.findByIsActiveTrue();
        } else {
            throw new ObjectNotFoundException("No pending restaurants present.");
        }

    }
}
