package org.example.sumatyw_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EnvProvider {

    private final Dotenv dotenv;

    public String getGoogleMapsApiKey() {
        return dotenv.get("GOOGLE_MAPS_API_KEY");
    }
}
