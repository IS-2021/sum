package org.example.sumatyw_backend.cities;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @GetMapping()
    public ResponseEntity<List<CityDTO>> getAllCities() {

        List<City> cities = cityService.getAllCities();

        return new ResponseEntity<>(
            cities.stream().map(CityDTOMapper::mapCityToCityDTO).toList(),
            HttpStatus.OK
        );
    }
}
