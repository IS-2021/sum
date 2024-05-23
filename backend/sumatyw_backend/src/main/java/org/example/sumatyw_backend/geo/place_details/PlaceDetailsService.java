package org.example.sumatyw_backend.geo.place_details;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlaceDetailsRequest.FieldMask;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class PlaceDetailsService {

    private final GeoApiContext geoApiContext;

    public PlaceDetails getPlaceDetails(String placeId) throws IOException, InterruptedException, ApiException {
        PlaceDetails placeDetails = new PlaceDetailsRequest(geoApiContext)
            .placeId(placeId)
            .language("pl")
            .fields(FieldMask.GEOMETRY_LOCATION_LAT, FieldMask.GEOMETRY_LOCATION_LNG, FieldMask.ADDRESS_COMPONENT)
            .await();
        placeDetails.placeId = placeId;

        return placeDetails;
    }

}
