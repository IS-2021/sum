package org.example.sumatyw_backend.restaurant_reports;

import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantReportRepository extends JpaRepository<RestaurantReport, UUID> {

    @Query("SELECT COUNT(r) > 0 FROM RestaurantReport r WHERE r.user.userId = :userId AND r.restaurant.restaurantId = :restaurantId AND r.isOpen = true")
    boolean existsByUserIdAndRestaurantId(@Param("userId") UUID userId, @Param("restaurantId") UUID restaurantId);

    List<RestaurantReport> findByIsOpenIsTrue();

    List<RestaurantReport> findAllByRestaurantRestaurantIdAndIsOpenTrue(UUID restaurantId);
}
