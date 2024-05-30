package org.example.sumatyw_backend.users;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.sumatyw_backend.addresses.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        User user1 = new User();
        user1.setUserId(UUID.randomUUID());
        User user2 = new User();
        user2.setUserId(UUID.randomUUID());
        List<User> users = Arrays.asList(user1, user2); // Mocked list of users
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(users.size()));

        verify(userService, times(1)).getUsers();
    }

    @Test
    public void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(); // Mocked user
        user.setUserId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetMe() throws Exception {
        User user = new User(); // Mocked user
        user.setUserId(UUID.randomUUID());
        when(userService.getMeUser()).thenReturn(user);

        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(user.getUserId().toString()));

        verify(userService, times(1)).getMeUser();
    }

    @Test
    public void testDeleteUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).removeUserById(userId);

        mockMvc.perform(delete("/users/{id}", userId))
            .andExpect(status().isNoContent());

        verify(userService, times(1)).removeUserById(userId);
    }

    @Test
    public void testUpdateUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        UserInputDTO userInputDTO = new UserInputDTO("firstName", "secondName", "username", "email@example.com", "P@ssw0rd123", "1234567890", Role.ROLE_USER);
        User user = UserDTOMapper.mapUserInputDTOToUser(userInputDTO);
        user.setUserId(userId);

        when(userService.updateUserById(eq(userId), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInputDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService, times(1)).updateUserById(eq(userId), any(User.class));
    }

    @Test
    public void testUpdateUserCity() throws Exception {
        UUID userId = UUID.randomUUID();
        String placeId = "somePlaceId";
        User user = new User(); // Mocked user
        user.setUserId(userId);

        when(userService.updateUserAddress(userId, placeId)).thenReturn(user);

        mockMvc.perform(post("/users/{userId}/address", userId)
                .param("placeId", placeId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService, times(1)).updateUserAddress(userId, placeId);
    }
}
