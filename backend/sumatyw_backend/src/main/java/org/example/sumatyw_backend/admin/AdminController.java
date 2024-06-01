package org.example.sumatyw_backend.admin;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.restaurants.*;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
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

            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
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

    @GetMapping( value = "/reports/restaurants", params = {"restaurantId"})
    public ResponseEntity<List<ReportDTO>> getAllRestaurantReports(@RequestParam("restaurantId") UUID restaurantId) {

            List<UserReport> list = userReportsService.getAllReportsUser(restaurantId);
            if(list.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
            }
            List<ReportDTO> reports = new ArrayList<>();

            for(UserReport report : list) {
                reports.add(ReportsDTOMapper.mapUserReportToReportDTO(report));
            }
            return new ResponseEntity<>(reports,HttpStatus.OK);
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

    @GetMapping( value = "/reports/users", params = {"userId"})
    public ResponseEntity<List<ReportDTO>> getAllUserReports(@RequestParam("userId") UUID userId) {

        List<RestaurantReport> list = restaurantReportsService.getAllReportsByRestaurant(userId);
        if(list.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }
        List<ReportDTO> reports = new ArrayList<>();

        for(RestaurantReport report : list) {
            reports.add(ReportsDTOMapper.mapRestaurantReportToReportDTO(report));
        }
        return new ResponseEntity<>(reports,HttpStatus.OK);
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
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
        }

    }


    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        List<RestaurantDTO> mappedList = restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList();

        return new ResponseEntity<>(mappedList, HttpStatus.OK);
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> activateRestaurant(@PathVariable("id") UUID id) {

        return new ResponseEntity<>(restaurantService.activateRestaurantById(id),HttpStatus.OK);
    }

}
