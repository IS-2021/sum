package org.example.sumatyw_backend.restaurants;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable("id") UUID id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<RestaurantDTO> deactivateRestaurantById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(restaurantService.deactivateRestaurant(id), HttpStatus.OK);
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByCity(@PathVariable("cityName") String city) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCity(city);

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantById(@PathVariable("id") UUID id) {
        restaurantService.removeRestaurantById(id);

        return new ResponseEntity<>(
            HttpStatus.NO_CONTENT
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurantById(@PathVariable("id") UUID id, @RequestBody @Valid RestaurantInputDTO restaurantInputDTO) {

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

            restaurant.setImageUUID(imageName);

            restaurantService.updateRestaurantImageUUID(restaurant);

            return new ResponseEntity<>("Restaurant image added successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add restaurant image", HttpStatus.BAD_REQUEST);
        }
    }
}
