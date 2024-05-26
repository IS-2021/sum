package org.example.sumatyw_backend.bookings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findBookingByMeal_RestaurantRestaurantId(UUID restaurantId);

    Optional<Booking> findByUserUserIdAndActiveIsTrue(UUID userId);

    Optional<Booking> findByMealMealIdAndActiveIsTrue(UUID mealId);

    List<Booking> findAllByUserUserId(UUID userId);
    
    List<Booking> findBookingByActiveIsTrue();
}
