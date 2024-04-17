package org.example.sumatyw_backend.restaurant_reports;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.users.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RestaurantReportsService {

    private final RestaurantReportRepository restaurantReportRepository;

    private final UserService userService;

    private final RestaurantService restaurantService;

    public RestaurantReport addRestaurantReport(ReportInputDTO reportInputDTO) {

        if(restaurantReportRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(),reportInputDTO.restaurantId())) {
            throw new ResourceAlreadyExistsException("This user has been already reported by this restaurant.");
        } else {

            RestaurantReport restaurantReport = ReportsDTOMapper.mapInputReportToRestaurantReport(reportInputDTO,
                userService.getUserById(reportInputDTO.userId()),
                restaurantService.getRestaurantById(reportInputDTO.restaurantId()));
            restaurantReport.setOpen(true);

            return restaurantReportRepository.save(restaurantReport);
        }
    }
}
