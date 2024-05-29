package org.example.sumatyw_backend.users;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.sumatyw_backend.addresses.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.users.*;

import java.util.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
            .userId(userId)
            .firstName("John")
            .secondName("Doe")
            .username("johndoe")
            .email("john@example.com")
            .password("password")
            .phoneNumber("1234567890")
            .role(Role.ROLE_USER)
            .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUsers() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("johndoe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    @WithMockUser(username = "johndoe", roles = {"USER"})
    void testGetMe() throws Exception {
        when(userService.getMeUser()).thenReturn(user);

        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUserById() throws Exception {
        doNothing().when(userService).removeUserById(userId);

        mockMvc.perform(delete("/users/{id}", userId))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUserById() throws Exception {
        UserInputDTO updatedUser = new UserInputDTO("Jane", "Doe", "johndoe", "jane@example.com","P@ssw0rd123","123456789",Role.ROLE_USER);
        User updated = User.builder()
            .userId(userId)
            .firstName("Jane")
            .secondName("Doe")
            .username("johndoe")
            .email("jane123@example.com")
            .phoneNumber("123456789")
            .password("P@ssw0rd123")
            .role(Role.ROLE_USER)
            .build();

        when(userService.updateUserById(eq(userId), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("jane123@example.com"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUserCity() throws Exception {
        String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";
        Address address = new Address("addressId", "City", "Street", "123", "PostalCode", "Country", null, 10.0, 10.0);
        user.setAddress(address);

        when(userService.updateUserAddress(userId, placeId)).thenReturn(user);

        mockMvc.perform(post("/users/{userId}/address", userId)
                .param("placeId", placeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.address.city").value("City"));
    }
}
