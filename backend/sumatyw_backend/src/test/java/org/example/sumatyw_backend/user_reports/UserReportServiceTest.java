package org.example.sumatyw_backend.user_reports;

import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.reports.ReportDTO;
import org.example.sumatyw_backend.reports.ReportInputDTO;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportRepository;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private User user;
    private Restaurant restaurant;
    private ReportInputDTO reportInputDTO;
    private UserReport userReport;
    private UUID reportId;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        reportId = UUID.randomUUID();

        user = new User();
        user.setUserId(userId);

        restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);

        reportInputDTO = new ReportInputDTO(userId, restaurantId, "Test cause");

        userReport = UserReport.builder()
            .userReportId(reportId)
            .user(user)
            .restaurant(restaurant)
            .cause("Test cause")
            .isOpen(true)
            .build();
    }

    @Test
    void testAddUserReport_Success() {
        when(userReportsRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(), reportInputDTO.restaurantId())).thenReturn(false);
        when(userService.getUserById(reportInputDTO.userId())).thenReturn(user);
        when(restaurantService.getRestaurantById(reportInputDTO.restaurantId())).thenReturn(restaurant);
        when(userReportsRepository.save(any(UserReport.class))).thenReturn(userReport);

        UserReport result = userReportsService.addUserReport(reportInputDTO);

        assertNotNull(result);
        assertEquals(userReport.getUserReportId(), result.getUserReportId());
        verify(userReportsRepository).save(any(UserReport.class));
    }

    @Test
    void testAddUserReport_AlreadyExists() {
        when(userReportsRepository.existsByUserIdAndRestaurantId(reportInputDTO.userId(), reportInputDTO.restaurantId())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userReportsService.addUserReport(reportInputDTO));
    }

    @Test
    void testGetAllUserReportsByUserId() {
        when(userReportsRepository.findAllByUserUserIdAndIsOpenTrue(user.getUserId())).thenReturn(List.of(userReport));

        List<UserReport> result = userReportsService.getAllUserReportsByUserId(user.getUserId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userReport.getUserReportId(), result.get(0).getUserReportId());
    }

    @Test
    void testCloseUserReport_Success() {
        when(userReportsRepository.findById(reportId)).thenReturn(Optional.of(userReport));
        userReport.setOpen(false);
        when(userReportsRepository.save(any(UserReport.class))).thenReturn(userReport);

        ReportDTO result = userReportsService.closeUserReport(reportId);

        assertNotNull(result);
        assertFalse(userReport.isOpen());
        verify(userReportsRepository).save(any(UserReport.class));
    }

    @Test
    void testCloseUserReport_NotFound() {
        when(userReportsRepository.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userReportsService.closeUserReport(reportId));
    }

    @Test
    void testGetUserReportById_Success() {
        when(userReportsRepository.findById(reportId)).thenReturn(Optional.of(userReport));

        UserReport result = userReportsService.getUserReportById(reportId);

        assertNotNull(result);
        assertEquals(userReport.getUserReportId(), result.getUserReportId());
    }

    @Test
    void testGetUserReportById_NotFound() {
        when(userReportsRepository.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userReportsService.getUserReportById(reportId));
    }

    @Test
    void testGetAllOpenedUserReports() {
        when(userReportsRepository.findByIsOpenIsTrue()).thenReturn(List.of(userReport));

        List<UserReport> result = userReportsService.getAllOpenedUserReports();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userReport.getUserReportId(), result.get(0).getUserReportId());
    }

    @Test
    void testGetAllUserReportByRestaurantId() {
        UUID restaurantId = restaurant.getRestaurantId();
        when(userReportsRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(restaurantId)).thenReturn(List.of(userReport));

        List<UserReport> result = userReportsService.getAllUserReportByRestaurantId(restaurantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userReport.getUserReportId(), result.get(0).getUserReportId());
    }

    @Test
    void testGetUserReportByRestaurantAndUserId() {
        UUID restaurantId = restaurant.getRestaurantId();
        UUID userId = user.getUserId();
        when(userReportsRepository.findAllByRestaurantRestaurantIdAndUserUserIdAndIsOpenTrue(restaurantId, userId)).thenReturn(List.of(userReport));

        List<UserReport> result = userReportsService.getUserReportByRestaurantAndUserId(restaurantId, userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userReport.getUserReportId(), result.get(0).getUserReportId());
    }

    @Test
    void testGetAllReportsUser() {
        UUID restaurantId = restaurant.getRestaurantId();
        when(userReportsRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(restaurantId)).thenReturn(List.of(userReport));

        List<UserReport> result = userReportsService.getAllReportsUser(restaurantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userReport.getUserReportId(), result.get(0).getUserReportId());
    }
    @Test
    void testGetAllReportsUser_EmptyList() {
        UUID restaurantId = restaurant.getRestaurantId();
        when(userReportsRepository.findAllByRestaurantRestaurantIdAndIsOpenTrue(restaurantId)).thenReturn(new ArrayList<>());

        List<UserReport> result = userReportsService.getAllReportsUser(restaurantId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
