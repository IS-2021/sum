package org.example.sumatyw_backend.geo.geocoding;

import com.google.maps.model.GeocodingResult;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressDTO;
import org.example.sumatyw_backend.addresses.AddressDTOMapper;
import org.example.sumatyw_backend.geo.AddressComponentMapper;

public class GeocodingMapper {

    public static AddressDTO mapReverseGeocodeToAddressDTO(GeocodingResult results) {
        Address addressComponents = AddressComponentMapper.mapAddressComponentsToAddress(results.addressComponents);
        addressComponents.setAddressId(results.placeId);

        return AddressDTOMapper.mapAddressToAddressDTO(addressComponents);
    }

}
