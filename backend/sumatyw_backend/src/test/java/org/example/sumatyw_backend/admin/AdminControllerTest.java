package org.example.sumatyw_backend.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.restaurant_reports.RestaurantReportsService;
import org.example.sumatyw_backend.restaurants.*;
import org.example.sumatyw_backend.user_reports.RestaurantReport;
import org.example.sumatyw_backend.user_reports.UserReport;
import org.example.sumatyw_backend.user_reports.UserReportsService;
import org.example.sumatyw_backend.users.Role;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private RestaurantReportsService restaurantReportsService;
    @Mock
    private UserReportsService userReportsService;

    @InjectMocks
    private AdminController adminController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUsers_ReturnsAllUsersWithGivenBlockedValue() throws Exception {
        // given
        List<User> users = Arrays.asList(
            User.builder().userId(UUID.randomUUID()).role(Role.ROLE_USER).blocked(true).build(),
            User.builder().userId(UUID.randomUUID()).role(Role.ROLE_USER).blocked(true).build(),
            User.builder().userId(UUID.randomUUID()).role(Role.ROLE_USER).blocked(true).build()
        );

        given(userService.getUsersByBlockedStatus(true)).willReturn(users);

        // when
        mockMvc.perform(get("/admin/users")
                .param("blocked", "true"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].blocked").value(true))
            .andExpect(jsonPath("$[1].blocked").value(true))
            .andExpect(jsonPath("$[2].blocked").value(true));

        verify(userService).getUsersByBlockedStatus(true);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUsers_ReturnsAllUsers() throws Exception {
        // given
        List<User> users = Arrays.asList(
            User.builder().userId(UUID.randomUUID()).role(Role.ROLE_USER).blocked(true).build(),
            User.builder().userId(UUID.randomUUID()).role(Role.ROLE_USER).blocked(true).build(),
            User.builder().userId(UUID.randomUUID()).role(Role.ROLE_USER).blocked(false).build()
        );

        given(userService.getUsers()).willReturn(users);

        // when
        mockMvc.perform(get("/admin/users"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].blocked").value(true))
            .andExpect(jsonPath("$[1].blocked").value(true))
            .andExpect(jsonPath("$[2].blocked").value(false));

        verify(userService).getUsers();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUserByID_ReturnsUserWithGivenId() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        User u1 = User.builder().userId(userId).role(Role.ROLE_USER).blocked(false).build();

        given(userService.getUserById(userId)).willReturn(u1);

        // when
        mockMvc.perform(get("/admin/users/{id}", userId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService).getUserById(userId);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateUser_BansGivenUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        User u1 = User.builder().userId(userId).role(Role.ROLE_USER).blocked(false).build();

        given(userService.getUserById(userId)).willReturn(u1);

        // when
        mockMvc.perform(put("/admin/users/{id}", userId)
                .param("ban", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u1)))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService).banUserById(any());
        verify(userService, never()).unbanUser(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateUser_UnbansGivenUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        User u1 = User.builder().userId(userId).role(Role.ROLE_USER).blocked(false).build();

        given(userService.getUserById(userId)).willReturn(u1);

        // when
        mockMvc.perform(put("/admin/users/{id}", userId)
                .param("ban", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u1)))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService, never()).banUserById(any());
        verify(userService).unbanUser(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllRestaurants_ReturnsAllRestaurants() throws Exception {
        // given
        User u1 = User.builder().userId(UUID.randomUUID()).role(Role.ROLE_RESTAURANT).blocked(false).build();

        Address a1 = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Address a2 = Address.builder().addressId("fhgdsgtgtgresf").city("Lodz").build();
        Address a3 = Address.builder().addressId("tujrhggdsgsfgs").city("Lodz").build();
        Address a4 = Address.builder().addressId("heyrtwrtwrtwre").city("Warsaw").build();
        Address a5 = Address.builder().addressId("ilujrhgrerthgt").city("Warsaw").build();

        List<Restaurant> restaurants = Arrays.asList(
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a2).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a5).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a3).status(RestaurantStatus.Banned).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a4).status(RestaurantStatus.Active).build()
        );
        given(restaurantService.getAllRestaurants()).willReturn(restaurants);

        // when
        mockMvc.perform(get("/admin/restaurants"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(5));

        verify(restaurantService).getAllRestaurants();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllRestaurants_ReturnsRestaurantsWithGivenStatus() throws Exception {
        // given
        User u1 = User.builder().userId(UUID.randomUUID()).role(Role.ROLE_RESTAURANT).blocked(false).build();

        Address a1 = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Address a2 = Address.builder().addressId("fhgdsgtgtgresf").city("Lodz").build();
        Address a5 = Address.builder().addressId("ilujrhgrerthgt").city("Warsaw").build();

        List<Restaurant> restaurants = Arrays.asList(
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a2).status(RestaurantStatus.Active).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a5).status(RestaurantStatus.Active).build()
        );
        given(restaurantService.getAllRestaurantsByStatus(RestaurantStatus.Active)).willReturn(restaurants);

        // when
        mockMvc.perform(get("/admin/restaurants")
                .param("status", "Active"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].status").value(RestaurantStatus.Active.toString()))
            .andExpect(jsonPath("$[1].status").value(RestaurantStatus.Active.toString()))
            .andExpect(jsonPath("$[2].status").value(RestaurantStatus.Active.toString()));

        verify(restaurantService).getAllRestaurantsByStatus(RestaurantStatus.Active);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getRestaurantById_ReturnsRestaurantWithGivenId() throws Exception {
        // given
        User u1 = User.builder().userId(UUID.randomUUID()).role(Role.ROLE_RESTAURANT).blocked(false).build();

        Address a1 = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).status(RestaurantStatus.Active).build();

        given(restaurantService.getRestaurantById(r1.getRestaurantId())).willReturn(r1);

        // when
        mockMvc.perform(get("/admin/restaurants/{id}", r1.getRestaurantId()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(r1.getRestaurantId().toString()));

        verify(restaurantService).getRestaurantById(r1.getRestaurantId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUserReportsByUserId_ReturnsAllOpenedUserReports() throws Exception {
        // given
        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build()
        );

        given(userReportsService.getAllOpenedUserReports()).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/users"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[2].isOpen").value(true));

        verify(userReportsService).getAllOpenedUserReports();
        verify(userReportsService, never()).getUserReportByRestaurantAndUserId(any(), any());
        verify(userReportsService, never()).getAllUserReportsByUserId(any());
        verify(userReportsService, never()).getAllUserReportByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUserReportsByUserId_ReturnsAllOpenedUserReportsForGivenRestaurant() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build()
        );

        given(userReportsService.getUserReportByRestaurantAndUserId(restaurantId, userId)).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/users")
                .param("userId", userId.toString())
                .param("restaurantId", restaurantId.toString())
            )
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[0].userId").value(userId.toString()))
            .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[1].userId").value(userId.toString()))
            .andExpect(jsonPath("$[1].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[2].isOpen").value(true))
            .andExpect(jsonPath("$[2].userId").value(userId.toString()))
            .andExpect(jsonPath("$[2].restaurantId").value(restaurantId.toString()));

        verify(userReportsService, never()).getAllOpenedUserReports();
        verify(userReportsService).getUserReportByRestaurantAndUserId(restaurantId, userId);
        verify(userReportsService, never()).getAllUserReportsByUserId(any());
        verify(userReportsService, never()).getAllUserReportByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUserReportsByUserId_ReturnsAllOpenedGivenUserReports() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build()
        );

        given(userReportsService.getAllUserReportsByUserId(userId)).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/users")
                .param("userId", userId.toString())
            )
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[0].userId").value(userId.toString()))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[1].userId").value(userId.toString()))
            .andExpect(jsonPath("$[2].isOpen").value(true))
            .andExpect(jsonPath("$[2].userId").value(userId.toString()));

        verify(userReportsService, never()).getAllOpenedUserReports();
        verify(userReportsService, never()).getUserReportByRestaurantAndUserId(any(), any());
        verify(userReportsService).getAllUserReportsByUserId(userId);
        verify(userReportsService, never()).getAllUserReportByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUserReportsByUserId_ReturnsAllOpenedUsersReportsForGivenRestaurant() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        List<UserReport> userReports = Arrays.asList(
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build()
        );

        given(userReportsService.getAllUserReportByRestaurantId(restaurantId)).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/users")
                .param("restaurantId", restaurantId.toString())
            )
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[1].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[2].isOpen").value(true))
            .andExpect(jsonPath("$[2].restaurantId").value(restaurantId.toString()));

        verify(userReportsService, never()).getAllOpenedUserReports();
        verify(userReportsService, never()).getUserReportByRestaurantAndUserId(any(), any());
        verify(userReportsService, never()).getAllUserReportsByUserId(any());
        verify(userReportsService).getAllUserReportByRestaurantId(restaurantId);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void changeRestaurantStatus_changesStatusOfGivenRestaurant() throws Exception {
        // given
        User u1 = User.builder().userId(UUID.randomUUID()).role(Role.ROLE_RESTAURANT).blocked(false).build();

        Address a1 = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();

        Restaurant r1 = Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).status(RestaurantStatus.Active).build();

        given(restaurantService.changeRestaurantStatus(r1.getRestaurantId(), RestaurantStatus.Banned)).willReturn(RestaurantDTOMapper.mapRestaurantToRestaurantDTO(r1));

        // when
        mockMvc.perform(put("/admin/restaurants/{id}", r1.getRestaurantId())
                .param("status", RestaurantStatus.Banned.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(restaurantService).changeRestaurantStatus(r1.getRestaurantId(), RestaurantStatus.Banned);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllOpenedRestaurantReports_ReturnsAllOpenedRestaurantReports() throws Exception {
        // given
        List<RestaurantReport> restaurantReports = Arrays.asList(
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build()
        );

        given(restaurantReportsService.getAllOpenedRestaurantReports()).willReturn(restaurantReports);

        //when
        mockMvc.perform(get("/admin/reports/restaurants"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[2].isOpen").value(true));

        verify(restaurantReportsService).getAllOpenedRestaurantReports();
        verify(restaurantReportsService, never()).getRestaurantReportsByUserIdRestaurantId(any(), any());
        verify(restaurantReportsService, never()).getAllReportsByUserId(any());
        verify(restaurantReportsService, never()).getRestaurantReportsByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllOpenedRestaurantReports_ReturnsAllOpenedRestaurantReportsForGivenUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        List<RestaurantReport> userReports = Arrays.asList(
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build()
        );

        given(restaurantReportsService.getRestaurantReportsByUserIdRestaurantId(userId, restaurantId)).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/restaurants")
                .param("userId", userId.toString())
                .param("restaurantId", restaurantId.toString())
            )
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[0].userId").value(userId.toString()))
            .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[1].userId").value(userId.toString()))
            .andExpect(jsonPath("$[1].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[2].isOpen").value(true))
            .andExpect(jsonPath("$[2].userId").value(userId.toString()))
            .andExpect(jsonPath("$[2].restaurantId").value(restaurantId.toString()));

        verify(restaurantReportsService, never()).getAllOpenedRestaurantReports();
        verify(restaurantReportsService).getRestaurantReportsByUserIdRestaurantId(userId, restaurantId);
        verify(restaurantReportsService, never()).getAllReportsByUserId(any());
        verify(restaurantReportsService, never()).getRestaurantReportsByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllOpenedRestaurantReports_ReturnsAllOpenedRestaurantsReportsForGivenUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        List<RestaurantReport> userReports = Arrays.asList(
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build()
        );

        given(restaurantReportsService.getAllReportsByUserId(userId)).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/restaurants")
                .param("userId", userId.toString())
            )
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[0].userId").value(userId.toString()))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[1].userId").value(userId.toString()))
            .andExpect(jsonPath("$[2].isOpen").value(true))
            .andExpect(jsonPath("$[2].userId").value(userId.toString()));

        verify(restaurantReportsService, never()).getAllOpenedRestaurantReports();
        verify(restaurantReportsService, never()).getRestaurantReportsByUserIdRestaurantId(any(), any());
        verify(restaurantReportsService).getAllReportsByUserId(userId);
        verify(restaurantReportsService, never()).getRestaurantReportsByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllOpenedRestaurantReports_ReturnsAllOpenedReportsOfGovenRestaurant() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        List<RestaurantReport> userReports = Arrays.asList(
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build(),
            RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(restaurantId).build()).user(User.builder().userId(userId).build()).build()
        );

        given(restaurantReportsService.getRestaurantReportsByRestaurantId(restaurantId)).willReturn(userReports);

        //when
        mockMvc.perform(get("/admin/reports/restaurants")
                .param("restaurantId", restaurantId.toString())
            )
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].isOpen").value(true))
            .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[1].isOpen").value(true))
            .andExpect(jsonPath("$[1].restaurantId").value(restaurantId.toString()))
            .andExpect(jsonPath("$[2].isOpen").value(true))
            .andExpect(jsonPath("$[2].restaurantId").value(restaurantId.toString()));

        verify(restaurantReportsService, never()).getAllOpenedRestaurantReports();
        verify(restaurantReportsService, never()).getRestaurantReportsByUserIdRestaurantId(userId, restaurantId);
        verify(restaurantReportsService, never()).getAllReportsByUserId(any());
        verify(restaurantReportsService).getRestaurantReportsByRestaurantId(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getRestaurantReportById_ReturnsRestaurantReportWithGivenId() throws Exception {
        // given
        RestaurantReport rr = RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(restaurantReportsService.getRestaurantReportById(rr.getRestaurantReportId())).willReturn(rr);

        //when
        mockMvc.perform(get("/admin/reports/restaurants/{id}", rr.getRestaurantReportId()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rr.getRestaurantReportId().toString()));

        verify(restaurantReportsService).getRestaurantReportById(rr.getRestaurantReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUserReportById_ReturnsUserReportWithGivenId() throws Exception {
        // given
        UserReport ur = UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(userReportsService.getUserReportById(ur.getUserReportId())).willReturn(ur);

        //when
        mockMvc.perform(get("/admin/reports/users/{id}", ur.getUserReportId()))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ur.getUserReportId().toString()));

        verify(userReportsService).getUserReportById(ur.getUserReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleRestaurantReport_BansReportedByRestaurantUser() throws Exception {
        // given
        RestaurantReport rr = RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(restaurantReportsService.getRestaurantReportById(rr.getRestaurantReportId())).willReturn(rr);

        //when
        mockMvc.perform(put("/admin/reports/restaurants/{id}", rr.getRestaurantReportId())
                .param("ban", "true"))
            // then
            .andExpect(status().isOk());

        verify(restaurantReportsService).getRestaurantReportById(rr.getRestaurantReportId());
        verify(userService).banUserById(rr.getUser().getUserId());
        verify(restaurantReportsService).closeRestaurantReport(rr.getRestaurantReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleRestaurantReport_ReturnsNotFound_IfGivenRestaurantReportDoesNotExist() throws Exception {
        // given
        RestaurantReport rr = RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(restaurantReportsService.getRestaurantReportById(rr.getRestaurantReportId())).willThrow(ObjectNotFoundException.class);

        //when
        mockMvc.perform(put("/admin/reports/restaurants/{id}", rr.getRestaurantReportId())
                .param("ban", "true"))
            // then
            .andExpect(status().isNotFound());

        verify(restaurantReportsService).getRestaurantReportById(rr.getRestaurantReportId());
        verify(userService, never()).banUserById(rr.getUser().getUserId());
        verify(restaurantReportsService, never()).closeRestaurantReport(rr.getRestaurantReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleRestaurantReport_ClosesRestaurantReport_IfBanParamIsFalse() throws Exception {
        // given
        RestaurantReport rr = RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        //when
        mockMvc.perform(put("/admin/reports/restaurants/{id}", rr.getRestaurantReportId())
                .param("ban", "false"))
            // then
            .andExpect(status().isOk());

        verify(restaurantReportsService, never()).getRestaurantReportById(rr.getRestaurantReportId());
        verify(userService, never()).banUserById(rr.getUser().getUserId());
        verify(restaurantReportsService).closeRestaurantReport(rr.getRestaurantReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleRestaurantReport_ReturnsNotFound_IfGivenRestaurantReportDoesNotExistAndBanParamIsFalse() throws Exception {
        // given
        RestaurantReport rr = RestaurantReport.builder().restaurantReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(restaurantReportsService.closeRestaurantReport(rr.getRestaurantReportId())).willThrow(ObjectNotFoundException.class);
        //when
        mockMvc.perform(put("/admin/reports/restaurants/{id}", rr.getRestaurantReportId())
                .param("ban", "false"))
            // then
            .andExpect(status().isNotFound());

        verify(restaurantReportsService, never()).getRestaurantReportById(rr.getRestaurantReportId());
        verify(userService, never()).banUserById(rr.getUser().getUserId());
        verify(restaurantReportsService).closeRestaurantReport(rr.getRestaurantReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleUserReport_BansReportedByUserRestaurant() throws Exception {
        // given
        UserReport ur = UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(userReportsService.getUserReportById(ur.getUserReportId())).willReturn(ur);

        //when
        mockMvc.perform(put("/admin/reports/users/{id}", ur.getUserReportId())
                .param("ban", "true"))
            // then
            .andExpect(status().isOk());

        verify(userReportsService).getUserReportById(ur.getUserReportId());
        verify(restaurantService).banRestaurantById(ur.getRestaurant().getRestaurantId());
        verify(userReportsService).closeUserReport(ur.getUserReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleUserReport_ReturnsNotFound_IfGivenUserReportDoesNotExist() throws Exception {
        // given
        UserReport ur = UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(userReportsService.getUserReportById(ur.getUserReportId())).willThrow(ObjectNotFoundException.class);

        //when
        mockMvc.perform(put("/admin/reports/users/{id}", ur.getUserReportId())
                .param("ban", "true"))
            // then
            .andExpect(status().isNotFound());

        verify(userReportsService).getUserReportById(ur.getUserReportId());
        verify(restaurantService, never()).banRestaurantById(ur.getRestaurant().getRestaurantId());
        verify(userReportsService, never()).closeUserReport(ur.getUserReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleUserReport_ClosesUserReport_IfBanParamIsFalse() throws Exception {
        // given
        UserReport ur = UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        //when
        mockMvc.perform(put("/admin/reports/users/{id}", ur.getUserReportId())
                .param("ban", "false"))
            // then
            .andExpect(status().isOk());

        verify(userReportsService, never()).getUserReportById(ur.getUserReportId());
        verify(restaurantService, never()).banRestaurantById(ur.getRestaurant().getRestaurantId());
        verify(userReportsService).closeUserReport(ur.getUserReportId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void handleUserReport_ReturnsNotFound_IfGivenUserReportDoesNotExistAndBanParamIsFalse() throws Exception {
        // given
        UserReport ur = UserReport.builder().userReportId(UUID.randomUUID()).isOpen(true).restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).user(User.builder().userId(UUID.randomUUID()).build()).build();

        given(userReportsService.closeUserReport(ur.getUserReportId())).willThrow(ObjectNotFoundException.class);

        //when
        mockMvc.perform(put("/admin/reports/users/{id}", ur.getUserReportId())
                .param("ban", "false"))
            // then
            .andExpect(status().isNotFound());

        verify(userReportsService, never()).getUserReportById(ur.getUserReportId());
        verify(restaurantService, never()).banRestaurantById(ur.getRestaurant().getRestaurantId());
        verify(userReportsService).closeUserReport(ur.getUserReportId());
    }
}
