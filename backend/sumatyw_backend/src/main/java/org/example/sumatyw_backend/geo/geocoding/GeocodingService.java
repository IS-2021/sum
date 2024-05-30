package org.example.sumatyw_backend.geo.geocoding;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ReverseGeocodeException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class GeocodingService {

    private final GeoApiContext geoApiContext;

    public GeocodingResult reverseGeocode(double lat, double lng) throws IOException, InterruptedException, ApiException {
        GeocodingResult[] results = GeocodingApi
            .reverseGeocode(geoApiContext, new LatLng(lat, lng))
            .language("pl")
            .resultType(AddressType.STREET_ADDRESS)
            .locationType(LocationType.ROOFTOP)
            .await();

        if (results.length == 0) {
            results = GeocodingApi
                .reverseGeocode(geoApiContext, new LatLng(lat, lng))
                .language("pl")
                .resultType(AddressType.STREET_ADDRESS)
                .await();
        }

        if (results.length == 0) {
            throw new ReverseGeocodeException(String.format("No address found for lat: %f, lng: %f", lat, lng));
        }
        return results[0];
    }

}
