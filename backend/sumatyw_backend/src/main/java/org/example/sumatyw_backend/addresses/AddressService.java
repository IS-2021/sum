package org.example.sumatyw_backend.addresses;

import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.geo.place_details.PlaceDetailsMapper;
import org.example.sumatyw_backend.geo.place_details.PlaceDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final PlaceDetailsService placeDetailsService;

    public Address getAddress(String addressId) throws IOException, InterruptedException, ApiException {
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            return address.get();
        }

        // If address is not present in the database, fetch it from Google Maps API
        PlaceDetails placeDetails = placeDetailsService.getPlaceDetails(addressId);
        Address newAddress = PlaceDetailsMapper.mapPlaceDetailsToAddress(placeDetails);

        return addressRepository.save(newAddress);
    }

}
