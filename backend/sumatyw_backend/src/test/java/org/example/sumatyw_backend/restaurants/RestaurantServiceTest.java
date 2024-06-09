package org.example.sumatyw_backend.restaurants;

import com.google.maps.errors.ApiException;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressService;
import org.example.sumatyw_backend.bookings.Status;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private RestaurantService restaurantService;

    private User user;
    private Restaurant restaurant;
    private Address address;
    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();

        user = new User();
        user.setUserId(userId);

        address = new Address();
        address.setAddressId(String.valueOf(UUID.randomUUID()));

        restaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .user(user)
            .address(address)
            .hours("{ \"monday\": [ \"10:00\", \"21:00\" ], \"tuesday\": [ \"08:00\", \"20:00\" ], \"wednesday\": [ \"10:00\", \"18:00\" ], \"thursday\": [ \"10:00\", \"20:00\" ], \"friday\": [ \"08:00\", \"23:00\" ], \"saturday\": [], \"sunday\": [ \"09:00\", \"22:00\" ] }")
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .status(RestaurantStatus.Inactive)
            .build();
    }

    @Test
    void testAddRestaurant_Success() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber())).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant result = restaurantService.addRestaurant(restaurant);

        assertNotNull(result);
        assertEquals(restaurant.getRestaurantId(), result.getRestaurantId());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testAddRestaurant_UserNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> restaurantService.addRestaurant(restaurant));
    }

    @Test
    void testAddRestaurant_UserHasRestaurant() {
        user.setRestaurant(restaurant);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class, () -> restaurantService.addRestaurant(restaurant));
    }

    @Test
    void testAddRestaurant_RestaurantPhoneNumberExists() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber())).thenReturn(Optional.of(restaurant));

        assertThrows(ResourceAlreadyExistsException.class, () -> restaurantService.addRestaurant(restaurant));
    }

    @Test
    void testBanRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        RestaurantDTO result = restaurantService.banRestaurantById(restaurantId);

        assertNotNull(result);
        assertEquals(RestaurantStatus.Banned, restaurant.getStatus());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testBanRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.banRestaurantById(restaurantId));
    }

    @Test
    void testGetAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<Restaurant> result = restaurantService.getAllRestaurants();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getRestaurantId(), result.get(0).getRestaurantId());
    }

    @Test
    void testGetAllActiveRestaurants() {
        when(restaurantRepository.findAllByStatus(RestaurantStatus.Active)).thenReturn(List.of(restaurant));

        List<Restaurant> result = restaurantService.getAllActiveRestaurants();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getRestaurantId(), result.get(0).getRestaurantId());
    }

    @Test
    void testGetRestaurantsByCity() {
        String city = "Test City";
        when(restaurantRepository.findAllByAddress_City_AndStatus(city, RestaurantStatus.Active)).thenReturn(List.of(restaurant));

        List<Restaurant> result = restaurantService.getRestaurantsByCity(city);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getRestaurantId(), result.get(0).getRestaurantId());
    }

    @Test
    void testDeactivateRestaurant_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        RestaurantDTO result = restaurantService.deactivateRestaurant(restaurantId);

        assertNotNull(result);
        assertEquals(RestaurantStatus.Inactive, restaurant.getStatus());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testDeactivateRestaurant_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.deactivateRestaurant(restaurantId));
    }

    @Test
    void testChangeRestaurantStatus_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        RestaurantDTO result = restaurantService.changeRestaurantStatus(restaurantId, RestaurantStatus.Active);

        assertNotNull(result);
        assertEquals(RestaurantStatus.Active, restaurant.getStatus());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testChangeRestaurantStatus_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.changeRestaurantStatus(restaurantId, RestaurantStatus.Active));
    }

    @Test
    void testGetRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(restaurantId);

        assertNotNull(result);
        assertEquals(restaurant.getRestaurantId(), result.getRestaurantId());
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.getRestaurantById(restaurantId));
    }

    @Test
    void testRemoveRestaurantById_Success() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        restaurantService.removeRestaurantById(restaurantId);

        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void testRemoveRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.removeRestaurantById(restaurantId));
    }

    @Test
    void testUpdateRestaurantById_Success() throws IOException, InterruptedException, ApiException {
        Restaurant updatedRestaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .user(user)
            .address(address)
            .name("Updated Restaurant")
            .phoneNumber("987654321")
            .status(RestaurantStatus.Active)
            .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(addressService.getAddress(address.getAddressId())).thenReturn(address);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        Restaurant result = restaurantService.updateRestaurantById(restaurantId, updatedRestaurant);

        assertNotNull(result);
        assertEquals("Updated Restaurant", result.getName());
        assertEquals("987654321", result.getPhoneNumber());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurantById_NotFound() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> restaurantService.updateRestaurantById(restaurantId, restaurant));
    }

    @Test
    void testGetAllPendingRestaurant() {
        when(restaurantRepository.findAllByStatus(RestaurantStatus.Inactive)).thenReturn(List.of(restaurant));

        List<Restaurant> result = restaurantService.getAllPendingRestaurant();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getRestaurantId(), result.get(0).getRestaurantId());
    }


    @Test
    public void testUpdateRestaurantImageUUID() {
        Restaurant sampleRestaurant = new Restaurant();
        restaurantService.updateRestaurantImageUUID(sampleRestaurant);

        verify(restaurantRepository).save(sampleRestaurant);
    }

    @Test
    public void testGetAllRestaurantsByStatus() {

        List<Restaurant> activeRestaurants = new ArrayList<>();
        activeRestaurants.add(restaurant);

        when(restaurantRepository.findAllByStatus(RestaurantStatus.Active)).thenReturn(activeRestaurants);

        List<Restaurant> fetchedRestaurants = restaurantService.getAllRestaurantsByStatus(RestaurantStatus.Active);

        assertEquals(activeRestaurants.size(), fetchedRestaurants.size());
        assertEquals(activeRestaurants, fetchedRestaurants);
    }
    @Test
    public void testUpdateRestaurantById() throws IOException, InterruptedException, ApiException {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(addressService.getAddress(any(String.class))).thenReturn(address);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant updatedRestaurant = restaurantService.updateRestaurantById(restaurantId, restaurant);

        assertEquals(restaurant, updatedRestaurant);
        verify(restaurantRepository).findById(restaurantId);
        verify(addressService).getAddress(any(String.class));
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    public void testUpdateRestaurantByIdThrowsObjectNotFoundException() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            restaurantService.updateRestaurantById(restaurantId, restaurant);
        });
        verify(restaurantRepository).findById(restaurantId);
    }
    @Test
    void testGetDistance() {
        double lat1 = 40.7128;
        double lon1 = -74.0060;
        double lat2 = 34.0522;
        double lon2 = -118.2437;

        double distance = restaurantService.getDistance(lat1, lon1, lat2, lon2);

        assertEquals(4980.046354221854, distance, 0.01);
    }

    @Test
    void testGetLocalRestaurants() {
        double userLat = 40.7128;
        double userLon = -74.0060;
        double radius = 1.0;

        Address address1 = new Address();
        address1.setLatitude(40.7158);
        address1.setLongitude(-74.0050);

        Address address2 = new Address();
        address2.setLatitude(40.7308);
        address2.setLongitude(-73.9970);

        Address address3 = new Address();
        address3.setLatitude(40.7138);
        address3.setLongitude(-74.0020);

        Restaurant restaurant1 = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .address(address1)
            .status(RestaurantStatus.Active)
            .build();

        Restaurant restaurant2 = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .address(address2)
            .status(RestaurantStatus.Active)
            .build();

        Restaurant restaurant3 = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .address(address3)
            .status(RestaurantStatus.Active)
            .build();

        List<Restaurant> allRestaurants = List.of(restaurant1, restaurant2, restaurant3);

        when(restaurantRepository.findAllByStatus(RestaurantStatus.Active)).thenReturn(allRestaurants);

        List<Restaurant> result = restaurantService.getLocalRestaurants(userLat, userLon, radius);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(restaurant1));
        assertFalse(result.contains(restaurant2));
        assertTrue(result.contains(restaurant3));
    }



}
