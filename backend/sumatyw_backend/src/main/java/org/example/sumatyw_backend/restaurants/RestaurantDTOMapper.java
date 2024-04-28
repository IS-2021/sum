package org.example.sumatyw_backend.restaurants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.addresses.AddressDTOMapper;
import org.example.sumatyw_backend.users.User;

public class RestaurantDTOMapper {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static Restaurant mapRestaurantInputDTOToRestaurant(RestaurantInputDTO restaurantInputDTO) throws JsonProcessingException {

            return Restaurant.builder()
                .name(restaurantInputDTO.name())
                .phoneNumber(restaurantInputDTO.phoneNumber())
                .address(AddressDTOMapper.mapAddressInputDTOToAddress(restaurantInputDTO.addressInputDTO()))
                .user(User.builder().userId(restaurantInputDTO.userId()).build())
                .hours(objectMapper.writeValueAsString(restaurantInputDTO.hours()))
                .build();

    }

    public static RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant) {

        try {
            return new RestaurantDTO(
                restaurant.getRestaurantId(),
                restaurant.getName(),
                restaurant.getPhoneNumber(),
                restaurant.getUser().getUserId(),
                AddressDTOMapper.mapAddressToAddressDTO(restaurant.getAddress()),
                objectMapper.readValue(restaurant.getHours(), Hours.class)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
