package org.example.sumatyw_backend.opinions;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OpinionService {
    private OpinionRepository opinionRepository;

    public Opinion addOpinion(Opinion opinion) {
        List<Opinion> userOpinions = opinionRepository.findAllByUser(User.builder().userId(opinion.getUser().getUserId()).build());

        for(Opinion o : userOpinions) {
            if (o.getRestaurant().getRestaurantId().equals(opinion.getRestaurant().getRestaurantId()))
                throw new ResourceAlreadyExistsException("User can only leave one opinion for a given restaurant");
        }

        return opinionRepository.save(opinion);
    }

    public List<Opinion> getOpinions(){
        return opinionRepository.findAll();
    }

    public Opinion updateOpinionById(UUID id, Opinion opinion) {
        Opinion existingOpinion = opinionRepository.findById(id).orElseThrow(RuntimeException::new);
        existingOpinion.setPositive(opinion.isPositive());
        existingOpinion.setTimestamp(opinion.getTimestamp());
        return opinionRepository.save(existingOpinion);
    }
}
