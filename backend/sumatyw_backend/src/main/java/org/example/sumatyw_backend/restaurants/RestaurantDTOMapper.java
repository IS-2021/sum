package org.example.sumatyw_backend.restaurants;

import org.example.sumatyw_backend.addresses.AddressDTOMapper;
import org.example.sumatyw_backend.users.User;

public class RestaurantDTOMapper {

    public static Restaurant mapRestaurantInputDTOToRestaurant(RestaurantInputDTO restaurantInputDTO) {
        return Restaurant.builder()
            .name(restaurantInputDTO.name())
            .phoneNumber(restaurantInputDTO.phoneNumber())
            .address(AddressDTOMapper.mapAddressInputDTOToAddress(restaurantInputDTO.addressInputDTO()))
            .user(User.builder().userId(restaurantInputDTO.userId()).build())
            .build();
    }

    public static RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant) {
       return new RestaurantDTO(
           restaurant.getRestaurantId(),
           restaurant.getName(),
           restaurant.getPhoneNumber(),
           restaurant.getUser().getUserId(),
           AddressDTOMapper.mapAddressToAddressDTO(restaurant.getAddress())
       );
    }
}
