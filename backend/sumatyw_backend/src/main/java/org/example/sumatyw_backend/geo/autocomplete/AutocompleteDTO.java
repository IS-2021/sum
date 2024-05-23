package org.example.sumatyw_backend.geo.autocomplete;

import lombok.Builder;

@Builder
public record AutocompleteDTO(String description, String placeId) {
}
