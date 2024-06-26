package org.example.sumatyw_backend.opinions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.users.User;
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

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OpinionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OpinionService opinionService;

    @InjectMocks
    private OpinionController opinionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(opinionController).build();
    }

    @Test
    @WithMockUser()
    public void testAddOpinion() throws Exception {
        OpinionInputDTO opinionInputDTO = new OpinionInputDTO(true, UUID.randomUUID(), UUID.randomUUID());

        Opinion opinion = Opinion.builder()
            .opinionId(UUID.randomUUID())
            .isPositive(opinionInputDTO.isPositive())
            .user(User.builder().userId(UUID.randomUUID()).build())
            .restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build())
            .timestamp(null)
            .build();

        when(opinionService.addOpinion(any(Opinion.class))).thenReturn(opinion);

        mockMvc.perform(post("/opinions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opinionInputDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.opinionId").exists())
            .andExpect(jsonPath("$.isPositive").value(opinionInputDTO.isPositive()));

        verify(opinionService, times(1)).addOpinion(any(Opinion.class));
    }

    @Test
    @WithMockUser()
    public void testUpdateOpinion() throws Exception {
        UUID opinionId = UUID.randomUUID();
        OpinionInputDTO opinionInputDTO = new OpinionInputDTO(true, UUID.randomUUID(), UUID.randomUUID());

        Opinion updatedOpinion = Opinion.builder()
            .opinionId(opinionId)
            .isPositive(opinionInputDTO.isPositive())
            .user(User.builder().userId(UUID.randomUUID()).build())
            .restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build())
            .timestamp(null)
            .build();

        when(opinionService.updateOpinionById(any(UUID.class), any(Opinion.class))).thenReturn(updatedOpinion);

        mockMvc.perform(put("/opinions/{id}", opinionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opinionInputDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.opinionId").value(opinionId.toString()))
            .andExpect(jsonPath("$.isPositive").value(opinionInputDTO.isPositive()));

        verify(opinionService, times(1)).updateOpinionById(any(UUID.class), any(Opinion.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetOpinionById() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        Opinion opinion = Opinion.builder()
            .opinionId(UUID.randomUUID())
            .isPositive(true)
            .user(User.builder().userId(userId).build())
            .restaurant(Restaurant.builder().restaurantId(restaurantId).build())
            .timestamp(null)
            .build();

        when(opinionService.getOpinionByUserIdRestaurantId(userId, restaurantId)).thenReturn(opinion);

        mockMvc.perform(get("/opinions")
                .param("userId", userId.toString())
                .param("restaurantId", restaurantId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.opinionId").value(opinion.getOpinionId().toString()))
            .andExpect(jsonPath("$.isPositive").value(opinion.isPositive()));

        verify(opinionService, times(1)).getOpinionByUserIdRestaurantId(userId, restaurantId);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteOpinionById() throws Exception {
        UUID opinionId = UUID.randomUUID();
        Opinion opinion = Opinion.builder()
            .opinionId(opinionId)
            .isPositive(true)
            .user(User.builder().userId(UUID.randomUUID()).build())
            .restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build())
            .timestamp(null)
            .build();

        when(opinionService.deleteOpinion(opinionId)).thenReturn(opinion);

        mockMvc.perform(delete("/opinions")
                .param("opinionId", opinionId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.opinionId").value(opinionId.toString()))
            .andExpect(jsonPath("$.isPositive").value(opinion.isPositive()));

        verify(opinionService, times(1)).deleteOpinion(opinionId);
    }

}



