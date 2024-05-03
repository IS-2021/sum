package org.example.sumatyw_backend.favourites;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Restaurant addFavourite(UUID userId, RestaurantFavouriteInputDTO favouriteInputDTO) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("User not found with ID: " + userId));
        Restaurant restaurantDB = restaurantRepository.findById(favouriteInputDTO.restaurantId())
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + favouriteInputDTO.restaurantId()));

        Optional<Favourite> favouriteDB = favouriteRepository.findByUserUserIdAndRestaurantRestaurantId(userId, favouriteInputDTO.restaurantId());

        if (favouriteDB.isPresent())
            throw new ResourceAlreadyExistsException("User with ID: " + userId + " already have restaurant with ID: " + favouriteInputDTO.restaurantId() + " in his favourites" );

        Favourite favourite = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(favouriteInputDTO.restaurantId()).build())
            .orderNumber(favouriteInputDTO.orderNumber())
            .build();

        favouriteRepository.save(favourite);
        return restaurantDB;
    }

    public List<Restaurant> getFavouritesByUserId(UUID userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("User not found with ID: " + userId));

        List<Favourite> favouriteList = favouriteRepository.findByUserUserId(userId)
                .stream().sorted(Comparator.comparing(Favourite::getOrderNumber)).toList();

        List<Restaurant> favouriteRestaurants = new ArrayList<>();

        for(Favourite f : favouriteList) {
            Restaurant r = restaurantRepository.findById(f.getRestaurant().getRestaurantId())
                .orElseThrow(() -> new ObjectNotFoundException("Restaurant not found with ID: " + f.getRestaurant().getRestaurantId()));

            favouriteRestaurants.add(r);
        }

        return favouriteRestaurants;
    }

    public void updateFavourite(UUID userId, List<RestaurantFavouriteInputDTO> favouriteList) {
        List<Favourite> favourites = favouriteRepository.findByUserUserId(userId);
        for (Favourite favourite : favourites) {
            for (RestaurantFavouriteInputDTO favouriteInputDTO : favouriteList) {
                if (favourite.getRestaurant().getRestaurantId().equals(favouriteInputDTO.restaurantId())) {
                    favourite.setOrderNumber(favouriteInputDTO.orderNumber());
                    favouriteRepository.save(favourite);
                }
            }
        }
    }

    public void deleteFavourite(DeleteFavouriteRestaurantsDTO deleteFavouriteRestaurantsDTO) {

        List<Favourite> favouriteList = favouriteRepository.findByUserUserId(deleteFavouriteRestaurantsDTO.userId());
        List<UUID> listOfIdsToDelete = deleteFavouriteRestaurantsDTO.restaurantIds();

        for (Favourite favourite : favouriteList) {
            if (listOfIdsToDelete.contains(favourite.getRestaurant().getRestaurantId())) {
                favouriteRepository.delete(favourite);
            }
        }
    }
}
