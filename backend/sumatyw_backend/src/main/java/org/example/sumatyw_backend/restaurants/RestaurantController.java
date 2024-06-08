package org.example.sumatyw_backend.restaurants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.errors.ApiException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantController {

    private RestaurantService restaurantService;
    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/images/";

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody @Valid RestaurantInputDTO restaurantInputDTO) {

        try {
            Restaurant restaurant = restaurantService.addRestaurant(
                RestaurantDTOMapper.mapRestaurantInputDTOToRestaurant(restaurantInputDTO));

            return new ResponseEntity<>(
                RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
                HttpStatus.OK
            );
        } catch (JsonProcessingException e) {
            throw  new InvalidDataException("Bad restaurant JSON");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','RESTAURANT')")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable("id") UUID id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<RestaurantDTO> deactivateRestaurantById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(restaurantService.deactivateRestaurant(id), HttpStatus.OK);
    }

    @GetMapping("/city/{cityName}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByCity(@PathVariable("cityName") String city) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCity(city);

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }
    
    @GetMapping(params = {"lat", "lon", "radius"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RestaurantDTO>> getLocalRestaurants(@RequestParam("lat") double lat,
                                                                   @RequestParam("lon") double lon,
                                                                   @RequestParam("radius") double radius) {

        List<Restaurant> restaurants = restaurantService.getLocalRestaurants(lat,lon,radius);

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT','ADMIN')")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable("id") UUID id) {
        restaurantService.removeRestaurantById(id);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<RestaurantDTO> updateRestaurantById(@PathVariable("id") UUID id, @RequestBody @Valid RestaurantInputDTO restaurantInputDTO) throws IOException, InterruptedException, ApiException {

        try {
            Restaurant restaurant = restaurantService.updateRestaurantById(
                id, RestaurantDTOMapper.mapRestaurantInputDTOToRestaurant(restaurantInputDTO)
            );

            return new ResponseEntity<>(
                RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
                HttpStatus.OK
            );
        } catch (JsonProcessingException e) {
            throw  new InvalidDataException("Bad restaurant JSON");
        }
    }

    @PostMapping("/images/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<String> addImage(@PathVariable("id") UUID restaurantId, @RequestParam("image") MultipartFile image) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        try {
            String imageName = UUID.randomUUID().toString();
            FileOutputStream fos = new FileOutputStream(IMAGE_UPLOAD_DIR + imageName + ".jpg");
            fos.write(image.getBytes());
            fos.close();

            if (!restaurant.getImageUUID().equals("default.jpg")) {
                File oldImageFile = new File(IMAGE_UPLOAD_DIR + restaurant.getImageUUID() + ".jpg");
                oldImageFile.delete();
            }

            restaurant.setImageUUID(imageName + ".jpg");

            restaurantService.updateRestaurantImageUUID(restaurant);

            return new ResponseEntity<>("Restaurant image added successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add restaurant image", HttpStatus.BAD_REQUEST);
        }
    }
}
