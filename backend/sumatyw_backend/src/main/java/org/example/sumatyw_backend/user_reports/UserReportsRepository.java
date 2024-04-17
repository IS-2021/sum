package org.example.sumatyw_backend.user_reports;

import org.example.sumatyw_backend.opinions.Opinion;
import org.example.sumatyw_backend.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserReportsRepository extends JpaRepository<UserReport, UUID> {

    @Query("SELECT COUNT(r) > 0 FROM UserReport r WHERE r.user.id = :userId AND r.restaurant.id = :restaurantId AND r.isOpen = true")
    boolean existsByUserIdAndRestaurantId(@Param("userId") UUID userId, @Param("restaurantId") UUID restaurantId);

}
