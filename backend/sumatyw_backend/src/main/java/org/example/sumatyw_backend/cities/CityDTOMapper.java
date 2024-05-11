package org.example.sumatyw_backend.cities;

public class CityDTOMapper {

    public static CityDTO mapCityToCityDTO(City city) {
        return new CityDTO(
            city.getCityId(),
            city.getName(),
            city.getRegion(),
            city.getCountry()
        );
    }
}

