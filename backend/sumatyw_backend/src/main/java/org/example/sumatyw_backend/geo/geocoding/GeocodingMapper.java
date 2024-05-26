package org.example.sumatyw_backend.geo.geocoding;

import com.google.maps.model.GeocodingResult;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressDTO;
import org.example.sumatyw_backend.addresses.AddressDTOMapper;
import org.example.sumatyw_backend.geo.AddressComponentMapper;

public class GeocodingMapper {

    public static AddressDTO mapReverseGeocodeToAddressDTO(GeocodingResult results) {
        Address address = AddressComponentMapper.mapAddressComponentsToAddress(results.addressComponents);
        address.setAddressId(results.placeId);
        address.setLatitude(results.geometry.location.lat);
        address.setLongitude(results.geometry.location.lng);

        return AddressDTOMapper.mapAddressToAddressDTO(address);
    }

}
