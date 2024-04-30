package org.example.sumatyw_backend.favourites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, UUID> {

    List<Favourite> findByUserUserId(UUID userId);

    Optional<Favourite> findByUserUserIdAndRestaurantRestaurantId(UUID userId, UUID restaurantId);
}
