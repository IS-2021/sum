package org.example.sumatyw_backend.opinions;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.restaurants.RestaurantStatus;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Opinion addOpinion(Opinion opinion) {
        Optional<Opinion> userRestaurantOpinion = opinionRepository
            .findByUserUserIdAndRestaurantRestaurantId(opinion.getUser().getUserId(), opinion.getRestaurant().getRestaurantId());

        userRepository.findById(opinion.getUser().getUserId()).orElseThrow(
            () -> new ObjectNotFoundException("User with id: " + opinion.getUser().getUserId() + " not found"));

        Restaurant restaurantDB = restaurantRepository.findById(opinion.getRestaurant().getRestaurantId())
            .orElseThrow(() -> new ObjectNotFoundException("Restaurant with id: " + opinion.getRestaurant().getRestaurantId() + " not found"));

        if (userRestaurantOpinion.isPresent())
            throw new ResourceAlreadyExistsException("User can only leave one opinion for a given restaurant");

        if (opinion.isPositive()) {
            restaurantDB.setLikesCount(restaurantDB.getLikesCount() + 1);
        } else {
            restaurantDB.setDislikesCount(restaurantDB.getDislikesCount() + 1);

            if (negativeOpinionThresholdExceeded(opinion.getRestaurant().getRestaurantId()))
                restaurantDB.setStatus(RestaurantStatus.Banned);
        }

        restaurantRepository.save(restaurantDB);

        return opinionRepository.save(opinion);
    }

    public Opinion getOpinionByUserIdRestaurantId(UUID userId,UUID restaurantId) {

        Optional<Opinion> opinion = opinionRepository.findByUserUserIdAndRestaurantRestaurantId(userId,restaurantId);

        if (opinion.isPresent()) {
            return opinion.get();
        } else {
            throw new ObjectNotFoundException("Opinion with user id: " + userId + " and restaurantId: "+ restaurantId + " not found");
        }
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

            if (negativeOpinionThresholdExceeded(opinion.getRestaurant().getRestaurantId()))
                restaurantDB.setStatus(RestaurantStatus.Banned);
        }

        existingOpinion.setPositive(opinion.isPositive());
        existingOpinion.setTimestamp(opinion.getTimestamp());
        restaurantRepository.save(restaurantDB);
        return opinionRepository.save(existingOpinion);
    }

    private boolean negativeOpinionThresholdExceeded(UUID restaurantId) {
        List<Opinion> lastWeekRestaurantOpinions = opinionRepository.findAllByRestaurantRestaurantId(restaurantId)
            .stream().filter(o -> o.getTimestamp().isAfter(LocalDateTime.now().minusWeeks(1))).toList();

        long lastWeekOpinionsCount = lastWeekRestaurantOpinions.size() + 1;

        if (lastWeekOpinionsCount >= 10) {
            long negativeOpinionsCount = lastWeekRestaurantOpinions.stream().filter(o -> o.isPositive() == false).count() + 1;

            return (double) negativeOpinionsCount / (double) lastWeekOpinionsCount > 0.7;
        }
        return false;
    }
}
