package org.example.sumatyw_backend.meals;

import org.example.sumatyw_backend.restaurants.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {

    Optional<Meal> findById(UUID id);

    List<Meal> findAllByRestaurantRestaurantId(UUID id);

}
