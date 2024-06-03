package org.example.sumatyw_backend.opinions;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.users.User;

import java.time.LocalDateTime;

public class OpinionDTOMapper {

    public static Opinion mapOpinionInputDTOToOpinion(OpinionInputDTO opinionInputDTO) {
        return Opinion.builder()
            .isPositive(opinionInputDTO.isPositive())
            .user(User.builder().userId(opinionInputDTO.userId()).build())
            .restaurant(Restaurant.builder().restaurantId(opinionInputDTO.restaurantId()).build())
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static OpinionDTO mapOpinionToOpinionDTO(Opinion opinion) {
        return new OpinionDTO(
            opinion.getOpinionId(),
            opinion.isPositive(),
            opinion.getUser().getUserId(),
            opinion.getRestaurant().getRestaurantId()
        );
    }
}
