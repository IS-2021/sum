package org.example.sumatyw_backend.restaurants;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Restaurant addRestaurant(Restaurant restaurant) {
        User userDB = userRepository.findById(restaurant.getUser().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + restaurant.getUser().getUserId()));

        if(userDB.getRestaurant() != null) {
            throw new ResourceAlreadyExistsException("User can have only one restaurant");
        }

        restaurant.setRestaurantId(restaurant.getUser().getUserId());
        restaurant.setImageUUID("default.jpg");
        restaurant.setStatus(RestaurantStatus.Inactive);

        if (this.restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Restaurant with phone number: '" + restaurant.getPhoneNumber() + "' already exists.");

        return restaurantRepository.save(restaurant);
    }

    public RestaurantDTO banRestaurantById(UUID id) {

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Restaurant with id: " + id + " not found"));
        restaurant.setStatus(RestaurantStatus.Banned);
        restaurantRepository.save(restaurant);
        return RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);

    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getAllActiveRestaurants() {
        return restaurantRepository.findAllByStatus(RestaurantStatus.Active);
    }

    public List<Restaurant> getAllRestaurantsByStatus(RestaurantStatus status) {
        return restaurantRepository.findAllByStatus(status);
    }

    public List<Restaurant> getRestaurantsByCity(String city) {
        return restaurantRepository.findAllByAddress_City_AndStatus(city, RestaurantStatus.Active);
    }


    public RestaurantDTO deactivateRestaurant(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Restaurant with id: " + id + " not found"));
        restaurant.setStatus(RestaurantStatus.Inactive);
        restaurantRepository.save(restaurant);
        return RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);
    }

    public RestaurantDTO changeRestaurantStatus(UUID id, RestaurantStatus status) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Restaurant with id: " + id + " not found"));
        restaurant.setStatus(status);
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
        existingRestaurant.setAddress(restaurant.getAddress());

        return restaurantRepository.save(existingRestaurant);
    }

    public List<Restaurant> getAllPendingRestaurant() {
        return restaurantRepository.findAllByStatus(RestaurantStatus.Inactive);
    }

    public void updateRestaurantImageUUID(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getLocalRestaurants(double userLat, double userLon, double radius) {
        List<Restaurant> restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.Active);

        return restaurants.stream().filter(r -> getDistance(userLat, userLon, r.getAddress().getLatitude(), r.getAddress().getLongitude()) <= radius).toList();
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point1 = geometryFactory.createPoint(new Coordinate(lon1, lat1));
        Point point2 = geometryFactory.createPoint(new Coordinate(lon2, lat2));
        DistanceOp distanceOp = new DistanceOp(point1, point2);

        return distanceOp.distance() * 111.32;

    }
}
