package org.example.sumatyw_backend.addresses;

import org.example.sumatyw_backend.cities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findByCityAndStreetAndNumberAndPostalCode (
        City city,
        String street,
        String number,
        String postalCode
    );

}
