package org.example.sumatyw_backend.geo.autocomplete;

import com.google.maps.model.AutocompletePrediction;

public record AutocompleteDTOMapper(String description, String placeId) {

    public static AutocompleteDTO mapPredictionToDTO(AutocompletePrediction prediction) {
        return AutocompleteDTO.builder()
            .placeId(prediction.placeId)
            .description(prediction.description)
            .build();
    }

}
