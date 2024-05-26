package org.example.sumatyw_backend.restaurants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByPhoneNumber(String phoneNumber);

    List<Restaurant> findAllByAddress_City_AndActiveTrue(String city);

    List<Restaurant> findAllByActiveFalse();

    List<Restaurant> findAllByActiveTrue();
}
