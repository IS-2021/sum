package org.example.sumatyw_backend.geo.autocomplete;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AutocompleteService {

    private final GeoApiContext context;

    public AutocompletePrediction[] autocompleteAddress(String query, UUID sessionToken) throws IOException, InterruptedException, ApiException {
        PlaceAutocompleteRequest.SessionToken st = new PlaceAutocompleteRequest.SessionToken(sessionToken);
        AutocompletePrediction[] response = PlacesApi.placeAutocomplete(context, query, st)
            .language("pl")
            .await();

        return response;
    }

}
