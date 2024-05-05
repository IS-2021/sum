package org.example.sumatyw_backend.opinions;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Opinion addOpinion(Opinion opinion) {
        // todo mozna sprawdzic czy istnieje opinia z user_id i restaurant_id zamiast pobierac calej listy
        List<Opinion> userOpinions = opinionRepository.findAllByUser(User.builder().userId(opinion.getUser().getUserId()).build());

        userRepository.findById(opinion.getUser().getUserId()).orElseThrow(
            () -> new ObjectNotFoundException("User with id: " + opinion.getUser().getUserId() + " not found"));

        Restaurant restaurantDB = restaurantRepository.findById(opinion.getRestaurant().getRestaurantId())
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant with id: " + opinion.getRestaurant().getRestaurantId() + " not found"));

        for(Opinion o : userOpinions) {
            if (o.getRestaurant().getRestaurantId().equals(opinion.getRestaurant().getRestaurantId()))
                throw new ResourceAlreadyExistsException("User can only leave one opinion for a given restaurant");
        }

        if (opinion.isPositive()) {
            restaurantDB.setLikesCount(restaurantDB.getLikesCount() + 1);
        } else {
            restaurantDB.setDislikesCount(restaurantDB.getDislikesCount() + 1);
        }

        restaurantRepository.save(restaurantDB);

        return opinionRepository.save(opinion);
    }

    public List<Opinion> getOpinions(){
        return opinionRepository.findAll();
    }

    public Opinion updateOpinionById(UUID id, Opinion opinion) {
        Opinion existingOpinion = opinionRepository.findById(id).orElseThrow(
            () -> new ObjectNotFoundException("Opinion with id: " + id + " not found")
        );

        userRepository.findById(opinion.getUser().getUserId()).orElseThrow(
            () -> new ObjectNotFoundException("User with id: " + opinion.getUser().getUserId() + " not found"));

        Restaurant restaurantDB = restaurantRepository.findById(opinion.getRestaurant().getRestaurantId())
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant with id: " + opinion.getRestaurant().getRestaurantId() + " not found"));

        if(opinion.isPositive()) {
            restaurantDB.setDislikesCount(restaurantDB.getDislikesCount() - 1);
            restaurantDB.setLikesCount(restaurantDB.getLikesCount() + 1);
        } else {
            restaurantDB.setLikesCount(restaurantDB.getLikesCount() - 1);
            restaurantDB.setDislikesCount(restaurantDB.getDislikesCount() + 1);
        }

        existingOpinion.setPositive(opinion.isPositive());
        existingOpinion.setTimestamp(opinion.getTimestamp());
        return opinionRepository.save(existingOpinion);
    }
}
