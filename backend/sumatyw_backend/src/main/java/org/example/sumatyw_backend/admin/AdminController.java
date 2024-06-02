package org.example.sumatyw_backend.admin;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.restaurants.*;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.user_reports.UserReportsService;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserDTO;
import org.example.sumatyw_backend.users.UserDTOMapper;
import org.example.sumatyw_backend.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam("blocked") boolean blocked) {
        List<User> users = userService.getUsersByBlockedStatus(blocked);

        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();

        return new ResponseEntity<>(
            users.stream().map(UserDTOMapper::mapUserToUserDTO).toList(),
            HttpStatus.OK
        );
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserByID(@PathVariable("id") UUID id) {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(
                UserDTOMapper.mapUserToUserDTO(user),
                HttpStatus.OK
            );
    }

    @GetMapping(value = "/restaurants")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping(value = "/restaurants", params = {"status"})
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(@RequestParam(value = "status") RestaurantStatus status) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurantsByStatus(status);

        return new ResponseEntity<>(
            restaurants.stream().map(RestaurantDTOMapper::mapRestaurantToRestaurantDTO).toList(),
            HttpStatus.OK
        );
    }

    @GetMapping(value = "/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable("id") UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        return new ResponseEntity<>(
            RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant),
            HttpStatus.OK
        );
    }

    @GetMapping( value = "/reports/users")
    public ResponseEntity<List<ReportDTO>> getAllUserReportsByUserId(@RequestParam(value = "userId", required = false) UUID userId,
                                                                     @RequestParam(value = "restaurantId", required = false) UUID restaurantId) {
        List<UserReport> userReports;

        if (userId == null && restaurantId == null) {
            userReports = userReportsService.getAllOpenedUserReports();
        }
        else if (userId != null && restaurantId == null) {
            userReports = userReportsService.getAllUserReportsByUserId(userId);
        }
        else if (restaurantId != null && userId == null){
            userReports = userReportsService.getAllUserReportByRestaurantId(restaurantId);
        } else {
            userReports = userReportsService.getUserReportByRestaurantAndUserId(restaurantId, userId);
        }

        return new ResponseEntity<>(
            userReports.stream().map(ReportsDTOMapper::mapUserReportToReportDTO).toList(),
            HttpStatus.OK
        );
    }

    @PutMapping(value = "/restaurants/{id}", params = {"status"})
    public ResponseEntity<RestaurantDTO> changeRestaurantStatus(@PathVariable("id") UUID id,
                                                                @RequestParam("status") RestaurantStatus status) {

        return new ResponseEntity<>(restaurantService.changeRestaurantStatus(id, status), HttpStatus.OK);
    }

//
//    @PutMapping("/users")
//    public ResponseEntity<UserDTO> unbanUser(@RequestBody UserDTO userDTO) {
//
//        return new ResponseEntity<>(UserDTOMapper.mapUserToUserDTO(userService.unbanUser(userDTO)),HttpStatus.OK);
//    }
//
//
//    @GetMapping("/reports/restaurants")
//    public ResponseEntity<List<ReportDTO>> getAllOpenedRestaurantReports() {
//
//        try {
//            return new ResponseEntity<>(restaurantReportsService.getAllOpenedRestaurantReports(),HttpStatus.OK);
//        } catch (ObjectNotFoundException e) {
//
//            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
//        }
//
//    }
//
//    @GetMapping("/reports/restaurants/{id}")
//    public ResponseEntity getRestaurantReportById(@PathVariable("id") UUID id) {
//
//        try {
//            return new ResponseEntity<>(ReportsDTOMapper.mapRestaurantReportToReportDTO(
//                restaurantReportsService.getRestaurantReportById(id)),HttpStatus.OK);
//        } catch (ObjectNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//    }
//
//    @GetMapping( value = "/reports/restaurants", params = {"restaurantId"})
//    public ResponseEntity<List<ReportDTO>> getAllRestaurantReports(@RequestParam("restaurantId") UUID restaurantId) {
//
//            List<UserReport> list = userReportsService.getAllReportsUser(restaurantId);
//            if(list.isEmpty()) {
//                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
//            }
//            List<ReportDTO> reports = new ArrayList<>();
//
//            for(UserReport report : list) {
//                reports.add(ReportsDTOMapper.mapUserReportToReportDTO(report));
//            }
//            return new ResponseEntity<>(reports,HttpStatus.OK);
//    }
//
//    @GetMapping("/reports/users/{id}")
//    public ResponseEntity getUserReportById(@PathVariable("id") UUID id) {
//
//        try {
//            return new ResponseEntity<>(ReportsDTOMapper.mapUserReportToReportDTO(
//                userReportsService.getUserReportById(id)),HttpStatus.OK);
//        } catch (ObjectNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//
//
//    @PutMapping(value = "/reports/users/{id}", params = {"ban"})
//    public ResponseEntity handleUserReport(@PathVariable("id") UUID reportId,
//                                           @RequestParam("ban") boolean ban) {
//
//        if(ban) {
//            try {
//                userService.banUserById(userReportsService.getUserReportById(reportId).getUser().getUserId());
//
//                return new ResponseEntity<>(userReportsService.closeUserReport(reportId),HttpStatus.OK);
//            } catch (ObjectNotFoundException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } else {
//            try {
//                return new ResponseEntity<>(userReportsService.closeUserReport(reportId),HttpStatus.OK);
//            } catch (ObjectNotFoundException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }
//
//    }
//
//    @PutMapping(value = "/reports/restaurants/{id}", params = "ban")
//    public ResponseEntity handleRestaurantReport(@PathVariable("id") UUID reportId,
//                                                 @RequestParam("ban") boolean ban) {
//        if(ban) {
//            try {
//                restaurantService.banRestaurantById(restaurantReportsService.getRestaurantReportById(reportId).getRestaurant().getRestaurantId());
//
//                return new ResponseEntity<>(restaurantReportsService.closeRestaurantReport(reportId),HttpStatus.OK);
//            } catch (ObjectNotFoundException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } else {
//            try {
//                return new ResponseEntity<>(restaurantReportsService.closeRestaurantReport(reportId),HttpStatus.OK);
//            } catch (ObjectNotFoundException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }
//    }
//
//    @GetMapping("/reports/users")
//    public ResponseEntity<List<ReportDTO>> getAllOpenedUserReports() {
//        try {
//            List<UserReport> userReports = userReportsService.getAllOpenedUserReports();
//            List<ReportDTO> mappedList = new ArrayList<>();
//
//            for(UserReport r: userReports) {
//                mappedList.add(ReportsDTOMapper.mapUserReportToReportDTO(r));
//            }
//            return new ResponseEntity<>(mappedList,HttpStatus.OK);
//
//        } catch (ObjectNotFoundException e) {
//            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
//        }
//
//    }
//

}
