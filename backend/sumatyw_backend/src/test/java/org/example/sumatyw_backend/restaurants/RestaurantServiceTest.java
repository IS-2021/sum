package org.example.sumatyw_backend.restaurants;

import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
    private RestaurantService restaurantService;
    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void addRestaurant_AddsNewRestaurant() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder()
            .user(user)
            .phoneNumber("123456789")
            .build();
        restaurant.setRestaurantId(user.getUserId());
        restaurant.setImageUUID("default.jpg");

        given(restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber())).willReturn(Optional.empty());
        given(restaurantRepository.save(restaurant)).willReturn(restaurant);

        // when
        Restaurant savedRestaurant = restaurantService.addRestaurant(restaurant);

        // then
        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(restaurantCaptor.capture());

        Restaurant capturedRestaurant = restaurantCaptor.getValue();

        assertThat(capturedRestaurant.getRestaurantId()).isEqualTo(user.getUserId());
        assertThat(capturedRestaurant.getImageUUID()).isEqualTo("default.jpg");
        assertThat(savedRestaurant).isEqualTo(capturedRestaurant);
    }

    @Test
    void addRestaurant_ThrowsResourceAlreadyExistsException_IfPhoneNumberExists() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder()
            .user(user)
            .phoneNumber("123456789")
            .build();

        given(restaurantRepository.findByPhoneNumber(restaurant.getPhoneNumber())).willReturn(Optional.of(restaurant));

        // when / then
        assertThatThrownBy(() -> restaurantService.addRestaurant(restaurant))
            .isInstanceOf(ResourceAlreadyExistsException.class)
            .hasMessageContaining("Restaurant with phone number: '123456789' already exists.");
    }

    @Test
    void banRestaurant_BanTheRestaurant() {
        // given
        UUID restaurantId = UUID.randomUUID();
        User user = User.builder().userId(UUID.randomUUID()).build();
        Address address = Address.builder().build();

        Restaurant restaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .hours("null")
            .imageUUID("default.jpg")
            .active(true)
            .user(user)
            .address(address)
            .build();

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        restaurantService.banRestaurantById(restaurantId);

        // then
        assertThat(restaurant.isBanned()).isTrue();
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void banRestaurantById_ThrowsObjectNotFoundException_IfRestaurantNotFound() {
        // given
        UUID restaurantId = UUID.randomUUID();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> restaurantService.banRestaurantById(restaurantId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant with id: " + restaurantId + " not found");
    }



    @Test
    void getAllRestaurants_ReturnsActiveRestaurants() {
        // given
        List<Restaurant> restaurants = List.of(Restaurant.builder().active(true).build());
        given(restaurantRepository.findAllByActiveTrue()).willReturn(restaurants);

        // when
        List<Restaurant> result = restaurantService.getAllRestaurants();

        // then
        assertThat(result).isEqualTo(restaurants);
        verify(restaurantRepository).findAllByActiveTrue();
    }

    @Test
    void getRestaurantsByCity_ReturnsActiveRestaurantsInCity() {
        // given
        String city = "SampleCity";
        List<Restaurant> restaurants = List.of(Restaurant.builder().active(true).build());
        given(restaurantRepository.findAllByAddress_City_AndActiveTrue(city)).willReturn(restaurants);

        // when
        List<Restaurant> result = restaurantService.getRestaurantsByCity(city);

        // then
        assertThat(result).isEqualTo(restaurants);
        verify(restaurantRepository).findAllByAddress_City_AndActiveTrue(city);
    }

    @Test
    void deactivateRestaurant_DeactivatesTheRestaurant() {
        // given
        UUID restaurantId = UUID.randomUUID();
        User user = User.builder().userId(UUID.randomUUID()).build();
        Address address = Address.builder().build();

        Restaurant restaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .hours("null")
            .active(true)
            .user(user)
            .address(address)
            .build();

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        restaurantService.deactivateRestaurant(restaurantId);

        // then
        assertThat(restaurant.isActive()).isFalse();
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void deactivateRestaurant_ThrowsObjectNotFoundException_IfRestaurantDoesNotExist() {
        // given
        UUID restaurantId = UUID.randomUUID();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> restaurantService.deactivateRestaurant(restaurantId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant with id: " + restaurantId + " not found");
    }

    @Test
    void activateRestaurantById_ActivatesTheRestaurant() {
        // given
        UUID restaurantId = UUID.randomUUID();
        User user = User.builder().userId(UUID.randomUUID()).build();
        Address address = Address.builder().build();

        Restaurant restaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .hours("null")
            .imageUUID("default.jpg")
            .active(false)
            .user(user)
            .address(address)
            .build();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        restaurantService.activateRestaurantById(restaurantId);

        // then
        assertThat(restaurant.isActive()).isTrue();
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void activateRestaurantById_ThrowsObjectNotFoundException_IfRestaurantDoesNotExist() {
        // given
        UUID restaurantId = UUID.randomUUID();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> restaurantService.activateRestaurantById(restaurantId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant with id: " + restaurantId + " not found");
    }

    @Test
    void getRestaurantById_ReturnsRestaurant() {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.builder().restaurantId(restaurantId).build();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        Restaurant result = restaurantService.getRestaurantById(restaurantId);

        // then
        assertThat(result).isEqualTo(restaurant);
    }

    @Test
    void getRestaurantById_ThrowsObjectNotFoundException_IfRestaurantDoesNotExist() {
        // given
        UUID restaurantId = UUID.randomUUID();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> restaurantService.getRestaurantById(restaurantId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant not found with ID: " + restaurantId);
    }

    @Test
    void removeRestaurantById_RemovesTheRestaurant() {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.builder().restaurantId(restaurantId).build();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant));

        // when
        restaurantService.removeRestaurantById(restaurantId);

        // then
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void removeRestaurantById_ThrowsObjectNotFoundException_IfRestaurantDoesNotExist() {
        // given
        UUID restaurantId = UUID.randomUUID();
        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> restaurantService.removeRestaurantById(restaurantId))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant not found with ID: " + restaurantId);
    }

    @Test
    void updateRestaurantById_UpdatesRestaurantDetails() {
        // given
        UUID restaurantId = UUID.randomUUID();
        User user = User.builder().userId(UUID.randomUUID()).build();
        Address address = Address.builder().build();
        Restaurant existingRestaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .hours("9:00 - 21:00")
            .imageUUID("default.jpg")
            .active(false)
            .user(user)
            .address(address)
            .build();
        Restaurant updatedRestaurant = Restaurant.builder().name("New Name").phoneNumber("456").hours("9-5").build();

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(existingRestaurant));
        given(restaurantRepository.findByPhoneNumber(updatedRestaurant.getPhoneNumber())).willReturn(Optional.empty());
        given(restaurantRepository.save(existingRestaurant)).willReturn(existingRestaurant);

        // when
        Restaurant result = restaurantService.updateRestaurantById(restaurantId, updatedRestaurant);

        // then
        assertThat(result.getName()).isEqualTo(updatedRestaurant.getName());
        assertThat(result.getPhoneNumber()).isEqualTo(updatedRestaurant.getPhoneNumber());
        assertThat(result.getHours()).isEqualTo(updatedRestaurant.getHours());
    }

    @Test
    void updateRestaurantById_ThrowsObjectNotFoundException_IfRestaurantDoesNotExist() {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant updatedRestaurant = Restaurant.builder().build();

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(restaurantId, updatedRestaurant))
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessageContaining("Restaurant not found with ID: " + restaurantId);
    }

}
