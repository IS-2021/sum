package org.example.sumatyw_backend.cities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    public Optional<City> findByName(String name);
    public Optional<City> findById(UUID id);
}
