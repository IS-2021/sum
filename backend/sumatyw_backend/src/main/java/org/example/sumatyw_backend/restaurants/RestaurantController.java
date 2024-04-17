package org.example.sumatyw_backend.restaurants;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantController {

    private RestaurantService restaurantService;

//    @PostMapping()
//    public addRestaurant(REstaurant)
}
