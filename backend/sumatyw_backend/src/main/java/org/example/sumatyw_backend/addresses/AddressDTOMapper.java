package org.example.sumatyw_backend.addresses;

public class AddressDTOMapper {

    public static Address mapAddressInputDTOToAddress(AddressInputDTO addressInputDTO) {
        return Address.builder()
            .addressId(addressInputDTO.addressId())
            .number(addressInputDTO.number())
            .street(addressInputDTO.street())
            .postalCode(addressInputDTO.postalCode())
            .city(addressInputDTO.city())
            .country(addressInputDTO.country())
            .latitude(addressInputDTO.latitude())
            .longitude(addressInputDTO.longitude())
            .build();
    }

    public static AddressDTO mapAddressToAddressDTO(Address address) {
        return new AddressDTO(
            address.getAddressId(),
            address.getNumber(),
            address.getStreet(),
            address.getPostalCode(),
            address.getCity(),
            address.getCountry(),
            address.getLatitude(),
            address.getLongitude()
        );
    }
}
