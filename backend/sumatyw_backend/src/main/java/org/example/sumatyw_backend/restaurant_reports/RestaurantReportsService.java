package org.example.sumatyw_backend.restaurant_reports;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.reports.ReportsDTOMapper;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.users.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<RestaurantReport> getAllReportsByRestaurant(UUID restaurantId) {

        List<RestaurantReport> list = restaurantReportRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(restaurantId);

        if(list.isEmpty()) {
            return new ArrayList<>();
        }
        return list;
    }

    public ReportDTO closeRestaurantReport(UUID restaurantReportId) {

        Optional<RestaurantReport> restaurantReport = restaurantReportRepository.findById(restaurantReportId);
        if(restaurantReport.isPresent()) {
            restaurantReport.get().setOpen(false);
            restaurantReportRepository.save(restaurantReport.get());
            return ReportsDTOMapper.mapRestaurantReportToReportDTO(restaurantReport.get());
        } else {
            throw new ObjectNotFoundException("This restaurant report does not exist.");
        }
    }

    public RestaurantReport getRestaurantReportById(UUID id) {

        if(restaurantReportRepository.findById(id).isPresent()) {
            return restaurantReportRepository.findById(id).get();
        } else {
            throw new ObjectNotFoundException("Restaurant report not found.");
        }

    }

    public List<ReportDTO> getAllOpenedRestaurantReports() {

        if(!restaurantReportRepository.findByIsOpenIsTrue().isEmpty()) {
        List<ReportDTO> mappedList = new ArrayList<>();
        List<RestaurantReport> restaurantReports = restaurantReportRepository.findByIsOpenIsTrue();

        for(RestaurantReport r: restaurantReports) {
            mappedList.add(ReportsDTOMapper.mapRestaurantReportToReportDTO(r));
        }

        return mappedList;
        } else {
        throw new ObjectNotFoundException("No opened reports present in database.");
        }
    }
}
