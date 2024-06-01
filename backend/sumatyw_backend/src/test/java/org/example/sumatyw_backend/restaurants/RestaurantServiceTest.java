package org.example.sumatyw_backend.restaurants;

import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private UUID restaurantId;
    private User user;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        user = User.builder().userId(UUID.randomUUID()).build();
        restaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .user(user)
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .hours("{ \"monday\": [ \"10:00\", \"21:00\" ], \"tuesday\": [ \"08:00\", \"20:00\" ], \"wednesday\": [ \"10:00\", \"18:00\" ], \"thursday\": [ \"10:00\", \"20:00\" ], \"friday\": [ \"08:00\", \"23:00\" ], \"saturday\": [], \"sunday\": [ \"09:00\", \"22:00\" ] }")
            .status(RestaurantStatus.Inactive)
            .address(Address.builder().city("Test City").build())
            .build();
    }


    @Test
    void testAddRestaurant_Success() {
        when(restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber())).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant result = restaurantService.addRestaurant(restaurant);

        assertNotNull(result);
        assertEquals(restaurant.getRestaurantId(), result.getRestaurantId());
        assertEquals("default.jpg", result.getImageUUID());
        assertEquals(RestaurantStatus.Inactive, result.getStatus());
        verify(restaurantRepository).findByPhoneNumber(restaurant.getPhoneNumber());
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testAddRestaurant_PhoneNumberExists() {
        when(restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber())).thenReturn(Optional.of(restaurant));

        assertThrows(ResourceAlreadyExistsException.class, () -> restaurantService.addRestaurant(restaurant));
        verify(restaurantRepository).findByPhoneNumber(restaurant.getPhoneNumber());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testBanRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantDTO result = restaurantService.banRestaurantById(restaurantId);

        assertNotNull(result);
        assertEquals(RestaurantStatus.Banned, restaurant.getStatus());
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testBanRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.banRestaurantById(restaurantId));
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testGetAllRestaurants() {
        List<Restaurant> restaurants = List.of(restaurant);
        when(restaurantRepository.findAllByStatus(RestaurantStatus.Active)).thenReturn(restaurants);

        List<Restaurant> result = restaurantService.getAllActiveRestaurants();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restaurantRepository).findAllByStatus(RestaurantStatus.Active);
    }

    @Test
    void testGetRestaurantsByCity() {
        List<Restaurant> restaurants = List.of(restaurant);
        when(restaurantRepository.findAllByAddress_City_AndStatus("Test City", RestaurantStatus.Active)).thenReturn(restaurants);

        List<Restaurant> result = restaurantService.getRestaurantsByCity("Test City");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restaurantRepository).findAllByAddress_City_AndStatus("Test City", RestaurantStatus.Active);
    }

    @Test
    void testDeactivateRestaurant_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantDTO result = restaurantService.deactivateRestaurant(restaurantId);

        assertNotNull(result);
        assertEquals(RestaurantStatus.Inactive, restaurant.getStatus());
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testDeactivateRestaurant_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.deactivateRestaurant(restaurantId));
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testActivateRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        RestaurantDTO result = restaurantService.activateRestaurantById(restaurantId);

        assertNotNull(result);
        assertEquals(RestaurantStatus.Active, restaurant.getStatus());
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testActivateRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.activateRestaurantById(restaurantId));
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testGetRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(restaurantId);

        assertNotNull(result);
        assertEquals(restaurantId, result.getRestaurantId());
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.getRestaurantById(restaurantId));
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void testRemoveRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        doNothing().when(restaurantRepository).deleteById(restaurantId);

        restaurantService.removeRestaurantById(restaurantId);

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void testRemoveRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.removeRestaurantById(restaurantId));
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testUpdateRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant updatedRestaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .user(user)
            .name("Updated Restaurant")
            .phoneNumber("987654321")
            .status(RestaurantStatus.Active)
            .build();

        Restaurant result = restaurantService.updateRestaurantById(restaurantId, updatedRestaurant);

        assertNotNull(result);
        assertEquals("Updated Restaurant", result.getName());
        assertEquals("987654321", result.getPhoneNumber());
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.updateRestaurantById(restaurantId, restaurant));
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testGetAllPendingRestaurant() {
        List<Restaurant> pendingRestaurants = List.of(restaurant);
        when(restaurantRepository.findAllByStatus(RestaurantStatus.Inactive)).thenReturn(pendingRestaurants);

        List<Restaurant> result = restaurantService.getAllPendingRestaurant();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restaurantRepository).findAllByStatus(RestaurantStatus.Inactive);
    }

    @Test
    void testUpdateRestaurantImageUUID() {
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        restaurantService.updateRestaurantImageUUID(restaurant);

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void testGetLocalRestaurants() {
        List<Restaurant> allRestaurants = List.of(
            Restaurant.builder().address(Address.builder().latitude(12.9715987).longitude(77.5945627).build()).status(RestaurantStatus.Active).build(),
            Restaurant.builder().address(Address.builder().latitude(13.035542).longitude(77.597100).build()).status(RestaurantStatus.Active).build()
        );
        when(restaurantRepository.findAllByStatus(RestaurantStatus.Active)).thenReturn(allRestaurants);

        List<Restaurant> result = restaurantService.getLocalRestaurants(12.9715987, 77.5945627, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restaurantRepository).findAllByStatus(RestaurantStatus.Active);
    }
}
