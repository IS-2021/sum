package org.example.sumatyw_backend.restaurants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByPhoneNumber(String phoneNumber);

    Optional<Restaurant> findByName(String name);

    List<Restaurant> findByIsActiveTrue();
//    Optional<Restaurant> findByUser(User user);
}
