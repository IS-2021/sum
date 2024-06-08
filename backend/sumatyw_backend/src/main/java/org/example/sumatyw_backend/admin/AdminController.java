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
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserDTO;
import org.example.sumatyw_backend.users.UserDTOMapper;
import org.example.sumatyw_backend.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/users", params = {"blocked"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam("blocked") boolean blocked) {
        List<User> users = userService.getUsersByBlockedStatus(blocked);

        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();

        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByID(@PathVariable("id") UUID id) {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(
                UserDTOMapper.mapUserToUserDTO(user),
                HttpStatus.OK
            );
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID userId,
                                              @RequestParam(value = "ban", required = true) boolean ban) {

        User user = userService.getUserById(userId);

        if(ban) {
            userService.banUserById(user.getUserId());
        } else {
            userService.unbanUser(user.getUserId());
        }
        return new ResponseEntity<>(UserDTOMapper.mapUserToUserDTO(user), HttpStatus.OK);
    }

    @GetMapping(value = "/restaurants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping(value = "/restaurants", params = {"status"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(@RequestParam(value = "status") RestaurantStatus status) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurantsByStatus(status);

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping(value = "/restaurants/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable("id") UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @GetMapping( value = "/reports/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDTO>> getAllUserReportsByUserId(@RequestParam(value = "userId", required = false) UUID userId,
                                                                     @RequestParam(value = "restaurantId", required = false) UUID restaurantId) {
        List<UserReport> userReports;

        if (userId == null && restaurantId == null) {
            userReports = userReportsService.getAllOpenedUserReports();
        }
        else if (userId != null && restaurantId != null) {
            userReports = userReportsService.getUserReportByRestaurantAndUserId(restaurantId, userId);
        }
        else if (userId != null) {
            userReports = userReportsService.getAllUserReportsByUserId(userId);
        } else {
            userReports = userReportsService.getAllUserReportByRestaurantId(restaurantId);
        }

        return new ResponseEntity<>(
            userReports.stream().map(ReportsDTOMapper::mapUserReportToReportDTO).toList(),
            HttpStatus.OK
        );
    }

    @PutMapping(value = "/restaurants/{id}", params = {"status"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantDTO> changeRestaurantStatus(@PathVariable("id") UUID id,
                                                                @RequestParam("status") RestaurantStatus status) {

        return new ResponseEntity<>(restaurantService.changeRestaurantStatus(id, status), HttpStatus.OK);
    }

    @GetMapping("/reports/restaurants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDTO>> getAllOpenedRestaurantReports(@RequestParam(value = "restaurantId",required = false) UUID restaurantId,
                                                                         @RequestParam(value = "userId",required = false) UUID userId) {
        List<RestaurantReport> restaurantReports;

        if(userId == null && restaurantId == null) {
            restaurantReports = restaurantReportsService.getAllOpenedRestaurantReports();
        }
        else if(userId != null && restaurantId != null) {
            restaurantReports = restaurantReportsService.getRestaurantReportsByUserIdRestaurantId(userId,restaurantId);
        }
        else if(userId != null) {
            restaurantReports = restaurantReportsService.getAllReportsByUserId(userId);
        } else {
             restaurantReports = restaurantReportsService.getRestaurantReportsByRestaurantId(restaurantId);
         }

        return new ResponseEntity<>(
            restaurantReports.stream().map(ReportsDTOMapper::mapRestaurantReportToReportDTO).toList(),
            HttpStatus.OK);

    }

    @GetMapping("/reports/restaurants/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> getRestaurantReportById(@PathVariable("id") UUID id) {

            return new ResponseEntity<>(ReportsDTOMapper.mapRestaurantReportToReportDTO(
                restaurantReportsService.getRestaurantReportById(id)),HttpStatus.OK);

        }

    @GetMapping("/reports/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> getUserReportById(@PathVariable("id") UUID id) {
            return new ResponseEntity<>(ReportsDTOMapper.mapUserReportToReportDTO(
                userReportsService.getUserReportById(id)),HttpStatus.OK);
    }


    @PutMapping(value = "/reports/restaurants/{id}", params = {"ban"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity handleRestaurantReport(@PathVariable("id") UUID reportId,
                                           @RequestParam(value = "ban") boolean ban) {

        if(ban) {
            try {
                RestaurantReport restaurantReport = restaurantReportsService.getRestaurantReportById(reportId);
                userService.banUserById(restaurantReport.getUser().getUserId());

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

    @PutMapping(value = "/reports/users/{id}", params = "ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity handleUserReport(@PathVariable("id") UUID reportId,
                                                 @RequestParam("ban") boolean ban) {
        if(ban) {

            try {
                UserReport userReport = userReportsService.getUserReportById(reportId);
                restaurantService.banRestaurantById(userReport.getRestaurant().getRestaurantId());

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


}
