package org.example.sumatyw_backend.user_reports;

import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantService;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserReportServiceTest {
    @Mock
    private UserReportsRepository userReportsRepository;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private UserReportsService userReportsService;

    @BeforeEach
    void setUp() {
        userReportsService = new UserReportsService(userReportsRepository, userService, restaurantService);
    }

    @Test
    void addUserReport_ShouldThrowResourceAlreadyExistsException_IfUserAlreadyReportedTheRestaurant() {
        // given
        ReportInputDTO reportInputDTO = new ReportInputDTO(UUID.randomUUID(), UUID.randomUUID(), "cause");
        given(userReportsRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(), reportInputDTO.restaurantId())).willReturn(true);

        // then
        assertThatThrownBy(() -> userReportsService.addUserReport(reportInputDTO))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    void addUserReport_ShouldAddNewReport_IfUserHasNotReportedTheRestaurant() {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        ReportInputDTO reportInputDTO = new ReportInputDTO(userId, restaurantId, "cause");
        given(userReportsRepository.existsByUserIdAndRestaurantId(userId, restaurantId)).willReturn(false);
        given(userService.getUserById(userId)).willReturn(new User());
        given(restaurantService.getRestaurantById(restaurantId)).willReturn(new Restaurant());

        // when
        userReportsService.addUserReport(reportInputDTO);

        // then
        verify(userService).getUserById(userId);
        verify(restaurantService).getRestaurantById(restaurantId);
        verify(userReportsRepository).save(any(UserReport.class));
    }

    @Test
    void closeUserReport_ShouldCloseReport_IfExists() {
        // given
        UUID reportId = UUID.randomUUID();
        User user = new User();
        Restaurant restaurant = new Restaurant();
        UserReport userReport = new UserReport();
        userReport.setOpen(true);
        userReport.setUser(user);
        userReport.setRestaurant(restaurant);
        given(userReportsRepository.findById(reportId)).willReturn(Optional.of(userReport));

        // when
        ReportDTO closedReport = userReportsService.closeUserReport(reportId);

        // then
        assertThat(closedReport.isOpen()).isFalse();
        verify(userReportsRepository).save(userReport);
    }

    @Test
    void closeUserReport_ShouldThrowObjectNotFoundException_IfReportDoesNotExist() {
        // given
        UUID reportId = UUID.randomUUID();
        given(userReportsRepository.findById(reportId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userReportsService.closeUserReport(reportId))
            .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getUserReportById_ShouldReturnReport_IfExists() {
        // given
        UUID reportId = UUID.randomUUID();
        UserReport userReport = new UserReport();
        given(userReportsRepository.findById(reportId)).willReturn(Optional.of(userReport));

        // when
        UserReport retrievedReport = userReportsService.getUserReportById(reportId);

        // then
        assertThat(retrievedReport).isEqualTo(userReport);
    }

    @Test
    void getUserReportById_ShouldThrowObjectNotFoundException_IfReportDoesNotExist() {
        // given
        UUID reportId = UUID.randomUUID();
        given(userReportsRepository.findById(reportId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userReportsService.getUserReportById(reportId))
            .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void getAllOpenedUserReports_ShouldReturnListOfReports_IfExist() {
        // given
        UserReport report1 = new UserReport();
        report1.setOpen(true);
        UserReport report2 = new UserReport();
        report2.setOpen(true);
        List<UserReport> userReports = Arrays.asList(report1, report2);
        given(userReportsRepository.findByIsOpenIsTrue()).willReturn(userReports);

        // when
        List<UserReport> retrievedReports = userReportsService.getAllOpenedUserReports();

        // then
        assertThat(retrievedReports.size()).isEqualTo(userReports.size());
    }

    @Test
    void getAllOpenedUserReports_ShouldThrowObjectNotFoundException_IfNoReportsExist() {
        // given
        given(userReportsRepository.findByIsOpenIsTrue()).willReturn(new ArrayList<>());

        // then
        assertThatThrownBy(() -> userReportsService.getAllOpenedUserReports())
            .isInstanceOf(ObjectNotFoundException.class);
    }
}
