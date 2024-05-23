package org.example.sumatyw_backend.geo;

import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressDTO;
import org.example.sumatyw_backend.addresses.AddressDTOMapper;
import org.example.sumatyw_backend.addresses.AddressService;
import org.example.sumatyw_backend.geo.autocomplete.AutocompleteDTO;
import org.example.sumatyw_backend.geo.autocomplete.AutocompleteDTOMapper;
import org.example.sumatyw_backend.geo.autocomplete.AutocompleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/geo")
@AllArgsConstructor
public class GeoController {

    private final AutocompleteService autocompleteService;
    private final AddressService addressService;

    @GetMapping("/autocomplete")
    public ResponseEntity<List<AutocompleteDTO>> autocompleteAddress(@RequestParam String query, @RequestParam UUID sessionToken) throws IOException, InterruptedException, ApiException {
        AutocompletePrediction[] searchResult = autocompleteService.autocompleteAddress(query, sessionToken);

        List<AutocompleteDTO> dtos = Arrays.stream(searchResult).map(AutocompleteDTOMapper::mapPredictionToDTO).toList();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/places")
    public ResponseEntity<AddressDTO> getPlaceDetails(@RequestParam String placeId) throws IOException, InterruptedException, ApiException {
        Address address = addressService.getAddress(placeId);

        AddressDTO dto = AddressDTOMapper.mapAddressToAddressDTO(address);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}