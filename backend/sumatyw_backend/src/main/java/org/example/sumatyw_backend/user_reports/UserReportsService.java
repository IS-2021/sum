package org.example.sumatyw_backend.user_reports;


import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.opinions.Opinion;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

//    public ReportDTO handleUserReport(UserReport userReport) {
//
//        if(userReportsRepository.findById(userReport.getUserReportId()).isPresent()) {
//            userReportsRepository.save(userReport);
//            return ReportsDTOMapper.mapUserReportToReportDTO(userReport);
//
//        } else {
//            throw new ObjectNotFoundException("User report not found.");
//        }
//
//    }

    public ReportDTO closeUserReport(UUID reportId) {

        Optional<UserReport> userReport = userReportsRepository.findById(reportId);
        if(userReport.isPresent()) {
            userReport.get().setOpen(false);
            userReportsRepository.save(userReport.get());
            return ReportsDTOMapper.mapUserReportToReportDTO(userReport.get());
        } else {
            throw new ObjectNotFoundException("User report not found.");
        }

    }

    public UserReport getUserReportById(UUID id) {

        if(userReportsRepository.findById(id).isPresent()) {
            return userReportsRepository.findById(id).get();
        } else {
            throw new ObjectNotFoundException("User report not found.");
        }

    }

    public List<UserReport> getAllOpenedUserReports() {

        if(!userReportsRepository.findByIsOpenTrue().isEmpty()) {
            return userReportsRepository.findByIsOpenTrue();
        } else {
            throw new ObjectNotFoundException("No opened reports present in database.");
        }

    }
}
