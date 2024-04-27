package org.example.sumatyw_backend.addresses;

import org.example.sumatyw_backend.cities.City;

public class AddressDTOMapper {

    public static Address mapAddressInputDTOToAddress(AddressInputDTO addressInputDTO) {
        return Address.builder()
            .number(addressInputDTO.number())
            .street(addressInputDTO.street())
            .postalCode(addressInputDTO.postalCode())
            .city(City.builder()
                .name(addressInputDTO.city())
                .country(addressInputDTO.country())
                .build())
            .build();
    }

    public static AddressDTO mapAddressToAddressDTO(Address address) {
        return new AddressDTO(
            address.getAddressId(),
            address.getNumber(),
            address.getStreet(),
            address.getPostalCode(),
            address.getCity().getName(),
            address.getCity().getCountry()
        );
    }
}
