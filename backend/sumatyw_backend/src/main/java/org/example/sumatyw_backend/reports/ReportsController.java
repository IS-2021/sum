package org.example.sumatyw_backend.reports;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.user_reports.UserReportsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class ReportsController {


    private final UserReportsService userReportsService;

    private final RestaurantReportsService restaurantReportsService;

    @GetMapping("/restaurants")
    public ResponseEntity<ReportDTO> addRestaurantReport(@RequestBody ReportInputDTO reportInputDTO) {
        RestaurantReport restaurantReport = restaurantReportsService.addRestaurantReport(reportInputDTO);

            return new ResponseEntity<>(
                ReportsDTOMapper.mapRestaurantReportToReportDTO(restaurantReport),
                HttpStatus.OK
            );
    }

    @GetMapping("/users")
    public ResponseEntity<ReportDTO> addUserReport(@RequestBody ReportInputDTO reportInputDTO) {
        UserReport userReport = userReportsService.addUserReport(reportInputDTO);

        return new ResponseEntity<>(
            ReportsDTOMapper.mapUserReportToReportDTO(userReport),
            HttpStatus.OK
        );
    }
}
