package org.example.sumatyw_backend.addresses;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address getAddress(String addressId) {
        Optional<Address> address = addressRepository.findByAddressId(addressId);

        if (address.isPresent()) {
            return address.get();
        }

        // TODO: Replace with call to geo.places API once it's implemented
        Address newAddress = Address.builder()
            .addressId(addressId)
            .city("Łódź")
            .street("Wólczańska")
            .number("215")
            .postalCode("93-005")
            .latitude(51.7487039)
            .longitude(19.4595253)
            .build();

        return addressRepository.save(newAddress);
    }

}
