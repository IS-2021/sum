package org.example.sumatyw_backend.bookings;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findBookingByMeal_RestaurantRestaurantId(UUID restaurantId, Sort sort);

    Optional<Booking> findByUserUserIdAndStatus(UUID userId, Status status);

    Optional<Booking> findByMealMealIdAndStatus(UUID mealId, Status status);

    List<Booking> findAllByMealMealId(UUID mealId);

    List<Booking> findAllByUserUserId(UUID userId,Sort sort);
    
    List<Booking> findByStatus(Status status,Sort sort);
}
