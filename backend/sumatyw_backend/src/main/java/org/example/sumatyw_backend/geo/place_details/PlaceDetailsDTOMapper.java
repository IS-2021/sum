package org.example.sumatyw_backend.geo.place_details;

import com.google.maps.model.PlaceDetails;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressDTO;

public class PlaceDetailsDTOMapper {

    public static AddressDTO mapPlaceDetailsToAddressDTO(PlaceDetails placeDetails) {
        Address addressComponents = AddressComponentMapper.mapPlaceDetailsToAddress(placeDetails.addressComponents);

        return new AddressDTO(
            placeDetails.placeId,
            addressComponents.getNumber(),
            addressComponents.getStreet(),
            addressComponents.getPostalCode(),
            addressComponents.getCity(),
            addressComponents.getCountry(),
            placeDetails.geometry.location.lat,
            placeDetails.geometry.location.lng
        );
    }

}
