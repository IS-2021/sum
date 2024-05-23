package org.example.sumatyw_backend.geo.context;

import com.google.maps.GeoApiContext;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.config.EnvProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class GmapsContext {

    private final EnvProvider envProvider;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
            .apiKey(envProvider.getGoogleMapsApiKey())
            .build();
    }

}
