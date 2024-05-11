package org.example.sumatyw_backend.bookings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookedMealsRepository extends JpaRepository<BookedMeals, UUID> {
}
