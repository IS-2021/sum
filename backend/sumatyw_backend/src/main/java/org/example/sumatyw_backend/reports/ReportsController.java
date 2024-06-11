package org.example.sumatyw_backend.reports;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.user_reports.UserReportsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class ReportsController {


    private final UserReportsService userReportsService;

    private final RestaurantReportsService restaurantReportsService;

    @PreAuthorize("hasAnyRole('USER', 'RESTAURANT')")
    @PostMapping("/restaurants")
    public ResponseEntity<ReportDTO> addRestaurantReport(@RequestBody @Valid ReportInputDTO reportInputDTO) {
        RestaurantReport restaurantReport = restaurantReportsService.addRestaurantReport(reportInputDTO);

            return new ResponseEntity<>(
                ReportsDTOMapper.mapRestaurantReportToReportDTO(restaurantReport),
                HttpStatus.OK
            );
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @GetMapping( value = "/restaurants", params = {"restaurantId"})
    public ResponseEntity<List<ReportDTO>> getAllRestaurantReports(@RequestParam("restaurantId") UUID restaurantId) {

        List<UserReport> list = userReportsService.getAllReportsUser(restaurantId);
        if(list.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
        }
        List<ReportDTO> reports = new ArrayList<>();

        for(UserReport report : list) {
            reports.add(ReportsDTOMapper.mapUserReportToReportDTO(report));
        }
        return new ResponseEntity<>(reports,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'RESTAURANT')")
    @PostMapping("/users")
    public ResponseEntity<ReportDTO> addUserReport(@RequestBody @Valid ReportInputDTO reportInputDTO) {
        UserReport userReport = userReportsService.addUserReport(reportInputDTO);

        return new ResponseEntity<>(
            ReportsDTOMapper.mapUserReportToReportDTO(userReport),
            HttpStatus.OK
        );
    }
}
