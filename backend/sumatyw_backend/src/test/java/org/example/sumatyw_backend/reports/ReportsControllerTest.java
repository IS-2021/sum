package org.example.sumatyw_backend.reports;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.reports.*;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.user_reports.UserReportsService;
import org.example.sumatyw_backend.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ReportsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserReportsService userReportsService;

    @Mock
    private RestaurantReportsService restaurantReportsService;

    @InjectMocks
    private ReportsController reportsController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();
    }

    @Test
    public void testAddRestaurantReport() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        ReportInputDTO reportInputDTO = new ReportInputDTO(userId, restaurantId, "Test cause");

        Restaurant restaurant = Restaurant.builder().restaurantId(restaurantId).build();

        RestaurantReport restaurantReport = RestaurantReport.builder()
            .restaurantReportId(UUID.randomUUID())
            .user(new User())
            .restaurant(restaurant)
            .cause(reportInputDTO.cause())
            .timestamp("2023-01-01T00:00:00Z")
            .isOpen(true)
            .build();

        when(restaurantReportsService.addRestaurantReport(any(ReportInputDTO.class))).thenReturn(restaurantReport);

        mockMvc.perform(post("/reports/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportInputDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.restaurantId").value(restaurantReport.getRestaurant().getRestaurantId().toString()));

        verify(restaurantReportsService, times(1)).addRestaurantReport(any(ReportInputDTO.class));
    }

    @Test
    public void testAddUserReport() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        ReportInputDTO reportInputDTO = new ReportInputDTO(userId, restaurantId, "Test cause");

        User user = User.builder().userId(userId).build();
        UserReport userReport = UserReport.builder()
            .userReportId(UUID.randomUUID())
            .user(user)
            .restaurant(new Restaurant())
            .cause(reportInputDTO.cause())
            .timestamp("2023-01-01T00:00:00Z")
            .isOpen(true)
            .build();

        when(userReportsService.addUserReport(any(ReportInputDTO.class))).thenReturn(userReport);

        mockMvc.perform(post("/reports/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportInputDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.userId").value(userReport.getUser().getUserId().toString()));

        verify(userReportsService, times(1)).addUserReport(any(ReportInputDTO.class));
    }

    @Test
    public void testAddRestaurantReportValidationError() throws Exception {
        ReportInputDTO reportInputDTO = new ReportInputDTO(null, null, "Test cause");

        mockMvc.perform(post("/reports/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportInputDTO)))
            .andExpect(status().isBadRequest());

    }

    @Test
    public void testAddUserReportValidationError() throws Exception {
        ReportInputDTO reportInputDTO = new ReportInputDTO(null, null, "Test cause");

        mockMvc.perform(post("/reports/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportInputDTO)))
            .andExpect(status().isBadRequest());
    }
}

