package org.example.sumatyw_backend.geo.place_details;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import org.example.sumatyw_backend.addresses.Address;

import java.util.Arrays;
import java.util.List;

public class AddressComponentMapper {

    public static Address mapPlaceDetailsToAddress(AddressComponent[] addressComponents) {
        Address.AddressBuilder address = Address.builder();

        String city = "";
        String street = "";
        String number = "";
        String postalCode = "";
        String country = "";

        for (AddressComponent component : addressComponents) {
            List<AddressComponentType> types = Arrays.asList(component.types);

            if (types.contains(AddressComponentType.LOCALITY)) {
                city = component.longName;
            } else if (types.contains(AddressComponentType.ROUTE)) {
                street = component.longName;
            } else if (types.contains(AddressComponentType.STREET_NUMBER)) {
                number = component.longName;
            } else if (types.contains(AddressComponentType.POSTAL_CODE)) {
                postalCode = component.longName;
            } else if (types.contains(AddressComponentType.COUNTRY)) {
                country = component.longName;
            }
        }

        return Address.builder()
            .city(city)
            .street(street)
            .number(number)
            .postalCode(postalCode)
            .country(country)
            .build();
    }

}
