package org.example.sumatyw_backend.admin;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantDTO;
import org.example.sumatyw_backend.restaurants.RestaurantDTOMapper;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.user_reports.UserReportsService;
import org.example.sumatyw_backend.users.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final RestaurantReportsService restaurantReportsService;
    private final UserReportsService userReportsService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }


    @GetMapping("/users/{id}")
    public ResponseEntity getUserByID(@PathVariable("id") UUID id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(
                UserDTOMapper.mapUserToUserDTO(user),
                HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/reports/restaurants")
    public ResponseEntity<List<ReportDTO>> getAllOpenedRestaurantReports() {

        try {
            return new ResponseEntity<>(restaurantReportsService.getAllOpenedRestaurantReports(),HttpStatus.OK);
        } catch (ObjectNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/reports/restaurants/{id}")
    public ResponseEntity getRestaurantReportById(@PathVariable("id") UUID id) {

        try {
            return new ResponseEntity<>(ReportsDTOMapper.mapRestaurantReportToReportDTO(
                restaurantReportsService.getRestaurantReportById(id)),HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/reports/users/{id}")
    public ResponseEntity getUserReportById(@PathVariable("id") UUID id) {

        try {
            return new ResponseEntity<>(ReportsDTOMapper.mapUserReportToReportDTO(
                userReportsService.getUserReportById(id)),HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reports/users/{id}")
    public ResponseEntity handleUserReport(@PathVariable("id") UUID reportId,
                                           @RequestParam("ban") boolean ban) {

        if(ban) {
            try {
                userService.banUserById(userReportsService.getUserReportById(reportId).getUser().getUserId());

                return new ResponseEntity<>(userReportsService.closeUserReport(reportId),HttpStatus.OK);
            } catch (ObjectNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            try {
                return new ResponseEntity<>(userReportsService.closeUserReport(reportId),HttpStatus.OK);
            } catch (ObjectNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

    }

    // kazdy dzien tygodnia osobno -- najlepiej obiekt
    // lokalizacja najblizszych restauracji
    // odleglosc w linii prostej

    @PutMapping("/reports/restaurants/{id}")
    public ResponseEntity handleRestaurantReport(@PathVariable("id") UUID reportId,
                                                 @RequestParam("ban") boolean ban) {
        if(ban) {
            try {
                restaurantService.banRestaurantById(restaurantReportsService.getRestaurantReportById(reportId).getRestaurant().getRestaurantId());

                return new ResponseEntity<>(restaurantReportsService.closeRestaurantReport(reportId),HttpStatus.OK);
            } catch (ObjectNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            try {
                return new ResponseEntity<>(restaurantReportsService.closeRestaurantReport(reportId),HttpStatus.OK);
            } catch (ObjectNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("/reports/users")
    public ResponseEntity<List<ReportDTO>> getAllOpenedUserReports() {

        try {
            List<UserReport> userReports = userReportsService.getAllOpenedUserReports();
            List<ReportDTO> mappedList = new ArrayList<>();

            for(UserReport r: userReports) {
                mappedList.add(ReportsDTOMapper.mapUserReportToReportDTO(r));
            }
            return new ResponseEntity<>(mappedList,HttpStatus.OK);

        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getAllPendingRestaurants() {

        try {
            List<Restaurant> restaurants = restaurantService.getAllPendingRestaurant();
            List<RestaurantDTO> mappedList = new ArrayList<>();

            for(Restaurant r: restaurants) {
                mappedList.add(RestaurantDTOMapper.mapRestaurantToRestaurantDTO(r));
            }

            return new ResponseEntity<>(mappedList,HttpStatus.OK);

        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
