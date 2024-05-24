package org.example.sumatyw_backend.geo.place_details;

import com.google.maps.model.PlaceDetails;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressDTO;
import org.example.sumatyw_backend.addresses.AddressDTOMapper;
import org.example.sumatyw_backend.geo.AddressComponentMapper;

public class PlaceDetailsMapper {

    public static AddressDTO mapPlaceDetailsToAddressDTO(PlaceDetails placeDetails) {
        Address address = AddressComponentMapper.mapAddressComponentsToAddress(placeDetails.addressComponents);
        address.setAddressId(placeDetails.placeId);
        address.setLatitude(placeDetails.geometry.location.lat);
        address.setLongitude(placeDetails.geometry.location.lng);

        return AddressDTOMapper.mapAddressToAddressDTO(address);
    }

    public static Address mapPlaceDetailsToAddress(PlaceDetails placeDetails) {
        Address addressComponents = AddressComponentMapper.mapAddressComponentsToAddress(placeDetails.addressComponents);

        return Address.builder()
            .addressId(placeDetails.placeId)
            .number(addressComponents.getNumber())
            .street(addressComponents.getStreet())
            .postalCode(addressComponents.getPostalCode())
            .city(addressComponents.getCity())
            .country(addressComponents.getCountry())
            .latitude(placeDetails.geometry.location.lat)
            .longitude(placeDetails.geometry.location.lng)
            .build();
    }

}
