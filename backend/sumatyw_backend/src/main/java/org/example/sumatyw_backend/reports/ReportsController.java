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

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class ReportsController {


    private final UserReportsService userReportsService;

    private final RestaurantReportsService restaurantReportsService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/restaurants")
    public ResponseEntity<ReportDTO> addRestaurantReport(@RequestBody @Valid ReportInputDTO reportInputDTO) {
        RestaurantReport restaurantReport = restaurantReportsService.addRestaurantReport(reportInputDTO);

            return new ResponseEntity<>(
                ReportsDTOMapper.mapRestaurantReportToReportDTO(restaurantReport),
                HttpStatus.OK
            );
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PostMapping("/users")
    public ResponseEntity<ReportDTO> addUserReport(@RequestBody @Valid ReportInputDTO reportInputDTO) {
        UserReport userReport = userReportsService.addUserReport(reportInputDTO);

        return new ResponseEntity<>(
            ReportsDTOMapper.mapUserReportToReportDTO(userReport),
            HttpStatus.OK
        );
    }
}
