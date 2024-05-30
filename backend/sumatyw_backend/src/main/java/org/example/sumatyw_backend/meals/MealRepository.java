package org.example.sumatyw_backend.meals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {

    List<Meal> findAllByRestaurantRestaurantId(UUID id);
}
