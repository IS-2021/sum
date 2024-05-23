package org.example.sumatyw_backend.geo.context;

import com.google.maps.GeoApiContext;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * Manages the lifecycle of the {@link GeoApiContext} bean.
 * As the docs say, it is recommended to shut down the context at the end of the application's lifecycle.
 * Otherwise, the thread will remain instantiated in the memory.
 *
 * @see <a href="https://github.com/googlemaps/google-maps-services-java?tab=readme-ov-file#usage">Relevant documentation fragment</a>
 */
@Component
@AllArgsConstructor
public class GeoApiContextManager implements DisposableBean {

    private final GeoApiContext geoApiContext;

    @Override
    public void destroy() {
        if (geoApiContext != null) {
            geoApiContext.shutdown();
        }
    }

}
