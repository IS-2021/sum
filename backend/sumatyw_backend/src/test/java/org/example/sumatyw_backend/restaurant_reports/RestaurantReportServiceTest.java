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
    private UUID userId;
    private UUID restaurantId;
    private UUID reportId;
    private ReportInputDTO reportInputDTO;
    private User user;
    private Restaurant restaurant;
    private RestaurantReport restaurantReport;
    private List<RestaurantReport> restaurantReports;

    @BeforeEach
    void setUp() {
        restaurantReportsService = new RestaurantReportsService(restaurantReportRepository, userService, restaurantService);
        userId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        reportId = UUID.randomUUID();
        reportInputDTO = new ReportInputDTO(userId, restaurantId, "cause");
        user = new User();
        restaurant = new Restaurant();
        restaurantReport = new RestaurantReport();
        restaurantReport.setUser(user);
        restaurantReport.setRestaurant(restaurant);
        restaurantReport.setOpen(true);
        RestaurantReport report1 = new RestaurantReport();
        report1.setUser(user);
        report1.setRestaurant(restaurant);
        report1.setOpen(true);
        RestaurantReport report2 = new RestaurantReport();
        report2.setRestaurant(restaurant);
        report2.setUser(user);
        report2.setOpen(true);
        restaurantReports = Arrays.asList(report1, report2);
    }

    @Test
    void addRestaurantReport_ShouldThrowResourceAlreadyExistsException_IfUserAlreadyReportedTheRestaurant() {
        // given
        given(restaurantReportRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(), reportInputDTO.restaurantId())).willReturn(true);

        // then
        assertThatThrownBy(() -> restaurantReportsService.addRestaurantReport(reportInputDTO))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void addRestaurantReport_ShouldAddNewReport_IfUserHasNotReportedTheRestaurant() {
        // given
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
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> restaurantReportsService.closeRestaurantReport(reportId))
            .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getRestaurantReportById_ShouldReturnReport_IfExists() {
        // given
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.of(restaurantReport));

        // when
        RestaurantReport retrievedReport = restaurantReportsService.getRestaurantReportById(reportId);

        // then
        assertThat(retrievedReport).isEqualTo(restaurantReport);
    }

    @Test
    void getRestaurantReportById_ShouldThrowObjectNotFoundException_IfReportDoesNotExist() {
        // given
        given(restaurantReportRepository.findById(reportId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> restaurantReportsService.getRestaurantReportById(reportId))
            .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getAllOpenedRestaurantReports_ShouldReturnListOfReports_IfExist() {
        // given
        given(restaurantReportRepository.findByIsOpenIsTrue()).willReturn(restaurantReports);

        // when
        List<RestaurantReport> retrievedReports = restaurantReportsService.getAllOpenedRestaurantReports();

        // then
        assertThat(retrievedReports.size()).isEqualTo(restaurantReports.size());
    }

    @Test
    void getAllOpenedRestaurantReports_ShouldReturnEmptyList_IfNoReportsExist() {
        // given
        given(restaurantReportRepository.findByIsOpenIsTrue()).willReturn(Collections.emptyList());

        // when
        List<RestaurantReport> result = restaurantReportsService.getAllOpenedRestaurantReports();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getAllReportsByUserId_ShouldReturnListOfReports_IfExist() {
        // given
        given(restaurantReportRepository.findAllByUserUserIdAndIsOpenTrue(userId)).willReturn(restaurantReports);

        // when
        List<RestaurantReport> retrievedReports = restaurantReportsService.getAllReportsByUserId(userId);

        // then
        assertThat(retrievedReports).isEqualTo(restaurantReports);
    }

    @Test
    void getAllReportsByUserId_ShouldReturnEmptyList_IfNoReportsExist() {
        // given
        given(restaurantReportRepository.findAllByUserUserIdAndIsOpenTrue(userId)).willReturn(Collections.emptyList());

        // when
        List<RestaurantReport> result = restaurantReportsService.getAllReportsByUserId(userId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getRestaurantReportsByUserIdRestaurantId_ShouldReturnListOfReports_IfExist() {
        // given
        given(restaurantReportRepository.findAllByUserUserIdAndRestaurantRestaurantIdAndIsOpenTrue(userId, restaurantId)).willReturn(restaurantReports);

        // when
        List<RestaurantReport> retrievedReports = restaurantReportsService.getRestaurantReportsByUserIdRestaurantId(userId, restaurantId);

        // then
        assertThat(retrievedReports).isEqualTo(restaurantReports);
    }

    @Test
    void getRestaurantReportsByUserIdRestaurantId_ShouldReturnEmptyList_IfNoReportsExist() {
        // given
        given(restaurantReportRepository.findAllByUserUserIdAndRestaurantRestaurantIdAndIsOpenTrue(userId, restaurantId)).willReturn(Collections.emptyList());

        // when
        List<RestaurantReport> result = restaurantReportsService.getRestaurantReportsByUserIdRestaurantId(userId, restaurantId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getRestaurantReportsByRestaurantId_ShouldReturnListOfReports_IfExist() {
        // given
        given(restaurantReportRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(restaurantId)).willReturn(restaurantReports);

        // when
        List<RestaurantReport> retrievedReports = restaurantReportsService.getRestaurantReportsByRestaurantId(restaurantId);

        // then
        assertThat(retrievedReports).isEqualTo(restaurantReports);
    }

    @Test
    void getRestaurantReportsByRestaurantId_ShouldReturnEmptyList_IfNoReportsExist() {
        // given
        given(restaurantReportRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(restaurantId)).willReturn(Collections.emptyList());

        // when
        List<RestaurantReport> result = restaurantReportsService.getRestaurantReportsByRestaurantId(restaurantId);

        // then
        assertThat(result).isEmpty();
    }
}
