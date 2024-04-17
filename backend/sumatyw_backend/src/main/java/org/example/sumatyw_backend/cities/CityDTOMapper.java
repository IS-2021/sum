package org.example.sumatyw_backend.cities;

public class CityDTOMapper {

    public static City mapCityInputDTOToCity(CityInputDTO cityInputDTO) {
        return City.builder()
            .name(cityInputDTO.name())
            .country(cityInputDTO.country())
            .build();
    }

    public static CityDTO mapCityToCityDTO(City city) {
        return new CityDTO(
            city.getCityId(),
            city.getName(),
            city.getCountry()
        );
    }
}
