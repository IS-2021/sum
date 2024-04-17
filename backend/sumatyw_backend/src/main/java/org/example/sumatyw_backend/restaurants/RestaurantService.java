package org.example.sumatyw_backend.restaurants;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant addRestaurant(Restaurant restaurant) {

        if (this.restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("Restaurant with phone number: '" + restaurant.getPhoneNumber() + "' already exists.");
        }

        // todo sprawdz czy adres jest unikalny

        return restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantById(UUID id) {
        return restaurantRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + id));
    }
}
