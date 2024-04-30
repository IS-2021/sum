package org.example.sumatyw_backend.users;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    List<User> findByBlockedFalse();
//    @Query("SELECT u.favouriteRestaurants FROM User u WHERE u.userId = :id")
//    List<Restaurant> findAllFavouriteRestaurantsByUserId(UUID id);

}
