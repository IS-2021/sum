package org.example.sumatyw_backend.restaurant_reports;

import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RestaurantReportServiceTest {

    @Mock
    private RestaurantReportRepository restaurantReportRepository;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantReportsService restaurantReportsService;

    @BeforeEach
    void setUp() {
        restaurantReportsService = new RestaurantReportsService(restaurantReportRepository,
            userService, restaurantService);
    }

    @Test
    void addRestaurantReport_ShouldThrowResourceAlreadyExistsException_IfUserAlreadyReportedTheRestaurant() {
        // given
        ReportInputDTO reportInputDTO = new ReportInputDTO(UUID.randomUUID(), UUID.randomUUID(), "cause");
        given(restaurantReportRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(), reportInputDTO.restaurantId())).willReturn(true);

        // then
        assertThatThrownBy(() -> restaurantReportsService.addRestaurantReport(reportInputDTO))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void addRestaurantReport_ShouldAddNewReport_IfUserHasNotReportedTheRestaurant() {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        ReportInputDTO reportInputDTO = new ReportInputDTO(userId, restaurantId, "cause");
        given(restaurantReportRepository.existsByUserIdAndRestaurantId(userId, restaurantId)).willReturn(false);

        // when
        restaurantReportsService.addRestaurantReport(reportInputDTO);

        // then
        verify(userService).getUserById(userId);
        verify(restaurantService).getRestaurantById(restaurantId);
        verify(restaurantReportRepository).save(any(RestaurantReport.class));
    }

    @Test
    void closeRestaurantReport_ShouldCloseReport_IfExists() {
        // given
        UUID reportId = UUID.randomUUID();
        User user = new User();
        Restaurant restaurant = new Restaurant();
        RestaurantReport restaurantReport = new RestaurantReport();
        restaurantReport.setUser(user);
        restaurantReport.setRestaurant(restaurant);
        restaurantReport.setOpen(true);
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.of(restaurantReport));

        // when
        ReportDTO closedReport = restaurantReportsService.closeRestaurantReport(reportId);

        // then
        assertThat(closedReport.isOpen()).isFalse();
        verify(restaurantReportRepository).save(restaurantReport);
    }

    @Test
    void closeRestaurantReport_ShouldThrowObjectNotFoundException_IfReportDoesNotExist() {
        // given
        UUID reportId = UUID.randomUUID();
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> restaurantReportsService.closeRestaurantReport(reportId))
            .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getRestaurantReportById_ShouldReturnReport_IfExists() {
        // given
        UUID reportId = UUID.randomUUID();
        RestaurantReport restaurantReport = new RestaurantReport();
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.of(restaurantReport));

        // when
        RestaurantReport retrievedReport = restaurantReportsService.getRestaurantReportById(reportId);

        // then
        assertThat(retrievedReport).isEqualTo(restaurantReport);
    }

    @Test
    void getRestaurantReportById_ShouldThrowObjectNotFoundException_IfReportDoesNotExist() {
        // given
        UUID reportId = UUID.randomUUID();
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> restaurantReportsService.getRestaurantReportById(reportId))
            .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getAllOpenedRestaurantReports_ShouldReturnListOfReports_IfExist() {
        // given
        User user = new User();
        Restaurant restaurant = new Restaurant();
        RestaurantReport report1 = new RestaurantReport();
        report1.setUser(user);
        report1.setRestaurant(restaurant);
        report1.setOpen(true);
        RestaurantReport report2 = new RestaurantReport();
        report2.setRestaurant(restaurant);
        report2.setUser(user);
        report2.setOpen(true);
        List<RestaurantReport> restaurantReports = Arrays.asList(report1, report2);
        given(restaurantReportRepository.findByIsOpenIsTrue()).willReturn(restaurantReports);

        // when
        List<ReportDTO> retrievedReports = restaurantReportsService.getAllOpenedRestaurantReports();

        // then
        assertThat(retrievedReports.size()).isEqualTo(restaurantReports.size());
    }

    @Test
    void getAllOpenedRestaurantReports_ShouldThrowObjectNotFoundException_IfNoReportsExist() {
        // given
        given(restaurantReportRepository.findByIsOpenIsTrue()).willReturn(new ArrayList<>());

        // then
        assertThatThrownBy(() -> restaurantReportsService.getAllOpenedRestaurantReports())
            .isInstanceOf(ObjectNotFoundException.class);
    }
}
