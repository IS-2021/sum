package org.example.sumatyw_backend.user_reports;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.opinions.Opinion;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserReportsService {

    private final UserReportsRepository userReportsRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public UserReport addUserReport(ReportInputDTO reportInputDTO) {
        if(userReportsRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(),reportInputDTO.restaurantId())) {
            throw new ResourceAlreadyExistsException("This restaurant has been already reported by this user.");
        } else {

            UserReport userReport = ReportsDTOMapper.mapInputReportToUserReport(reportInputDTO,
                userService.getUserById(reportInputDTO.userId()), restaurantService.getRestaurantById(reportInputDTO.restaurantId()));

            userReport.setOpen(true);
            return userReportsRepository.save(userReport);
        }
    }
}
