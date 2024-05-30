package org.example.sumatyw_backend.favourites;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FavouritesServiceTest {

    @InjectMocks
    private FavouriteService favouriteService;

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FavouriteRepository favouriteRepository;

    private UUID userId;
    private RestaurantFavouriteInputDTO favouriteInputDTO;
    private Restaurant restaurant;
    private User user;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        favouriteInputDTO = new RestaurantFavouriteInputDTO(UUID.randomUUID(), 1);
        restaurant = Restaurant.builder().restaurantId(favouriteInputDTO.restaurantId()).build();
        user = User.builder().userId(userId).build();
    }

    @Test
    void testAddFavourite_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(favouriteInputDTO.restaurantId())).thenReturn(Optional.of(restaurant));
        when(favouriteRepository.findByUserUserIdAndRestaurantRestaurantId(userId, favouriteInputDTO.restaurantId()))
            .thenReturn(Optional.empty());

        Restaurant result = favouriteService.addFavourite(userId, favouriteInputDTO);

        assertEquals(restaurant, result);
        verify(favouriteRepository).save(any(Favourite.class));
    }

    @Test
    void testAddFavourite_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            favouriteService.addFavourite(userId, favouriteInputDTO);
        });

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    void testAddFavourite_RestaurantNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(favouriteInputDTO.restaurantId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            favouriteService.addFavourite(userId, favouriteInputDTO);
        });

        assertEquals("Restaurant not found with ID: " + favouriteInputDTO.restaurantId(), exception.getMessage());
    }

    @Test
    void testAddFavourite_AlreadyExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(favouriteInputDTO.restaurantId())).thenReturn(Optional.of(restaurant));
        when(favouriteRepository.findByUserUserIdAndRestaurantRestaurantId(userId, favouriteInputDTO.restaurantId()))
            .thenReturn(Optional.of(new Favourite()));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            favouriteService.addFavourite(userId, favouriteInputDTO);
        });

        assertEquals("User with ID: " + userId + " already have restaurant with ID: " + favouriteInputDTO.restaurantId() + " in his favourites", exception.getMessage());
    }

    @Test
    void testGetFavouritesByUserId_Success() {
        UUID restaurantId1 = UUID.randomUUID();
        UUID restaurantId2 = UUID.randomUUID();

        Favourite favourite1 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId1).build())
            .orderNumber(1)
            .build();

        Favourite favourite2 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId2).build())
            .orderNumber(2)
            .build();

        List<Favourite> favourites = List.of(favourite1, favourite2);
        Restaurant restaurant1 = Restaurant.builder().restaurantId(restaurantId1).build();
        Restaurant restaurant2 = Restaurant.builder().restaurantId(restaurantId2).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(favouriteRepository.findByUserUserId(userId)).thenReturn(favourites);
        when(restaurantRepository.findById(restaurantId1)).thenReturn(Optional.of(restaurant1));
        when(restaurantRepository.findById(restaurantId2)).thenReturn(Optional.of(restaurant2));

        List<Restaurant> result = favouriteService.getFavouritesByUserId(userId);

        assertEquals(List.of(restaurant1, restaurant2), result);
    }

    @Test
    void testGetFavouritesByUserId_RestaurantNotFound() {
        UUID restaurantId1 = UUID.randomUUID();

        Favourite favourite1 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId1).build())
            .orderNumber(1)
            .build();

        List<Favourite> favourites = List.of(favourite1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(favouriteRepository.findByUserUserId(userId)).thenReturn(favourites);
        when(restaurantRepository.findById(restaurantId1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            favouriteService.getFavouritesByUserId(userId);
        });

        assertEquals("Restaurant not found with ID: " + restaurantId1, exception.getMessage());
    }

    @Test
    void testUpdateFavourite_Success() {
        UUID restaurantId1 = UUID.randomUUID();
        UUID restaurantId2 = UUID.randomUUID();

        RestaurantFavouriteInputDTO favouriteInputDTO1 = new RestaurantFavouriteInputDTO(restaurantId1, 1);
        RestaurantFavouriteInputDTO favouriteInputDTO2 = new RestaurantFavouriteInputDTO(restaurantId2, 2);
        List<RestaurantFavouriteInputDTO> favouriteInputDTOs = List.of(favouriteInputDTO1, favouriteInputDTO2);

        Favourite favourite1 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId1).build())
            .orderNumber(0)
            .build();

        Favourite favourite2 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId2).build())
            .orderNumber(0)
            .build();

        List<Favourite> favourites = List.of(favourite1, favourite2);

        when(favouriteRepository.findByUserUserId(userId)).thenReturn(favourites);

        favouriteService.updateFavourite(userId, favouriteInputDTOs);

        verify(favouriteRepository).save(favourite1);
        verify(favouriteRepository).save(favourite2);

        assertEquals(1, favourite1.getOrderNumber());
        assertEquals(2, favourite2.getOrderNumber());
    }

    @Test
    void testDeleteFavourite_Success() {
        UUID restaurantId1 = UUID.randomUUID();
        UUID restaurantId2 = UUID.randomUUID();
        List<UUID> restaurantIdsToDelete = List.of(restaurantId1, restaurantId2);

        DeleteFavouriteRestaurantsDTO deleteFavouriteRestaurantsDTO = new DeleteFavouriteRestaurantsDTO(userId, restaurantIdsToDelete);

        Favourite favourite1 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId1).build())
            .build();

        Favourite favourite2 = Favourite.builder()
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId2).build())
            .build();

        List<Favourite> favourites = List.of(favourite1, favourite2);

        when(favouriteRepository.findByUserUserId(userId)).thenReturn(favourites);

        favouriteService.deleteFavourite(deleteFavouriteRestaurantsDTO);

        verify(favouriteRepository, times(1)).delete(favourite1);
        verify(favouriteRepository, times(1)).delete(favourite2);
    }
}

