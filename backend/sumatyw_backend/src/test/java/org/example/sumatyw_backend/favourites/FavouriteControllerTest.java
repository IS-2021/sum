package org.example.sumatyw_backend.favourites;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.users.Role;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
class FavouriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavouriteService favouriteService;

    @InjectMocks
    private FavouriteController favouriteController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(favouriteController).build();
    }

    @Test
    @WithMockUser()
    void addFavourite_AddNewUserFavouriteRestaurant() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        RestaurantFavouriteInputDTO f = new RestaurantFavouriteInputDTO(UUID.randomUUID(), 1);

        given(favouriteService.addFavourite(userId, f)).willReturn(Restaurant.builder()
            .restaurantId(f.restaurantId())
            .user(User.builder().userId(userId).build())
            .address(Address.builder().addressId("asdgfgfasdfad").build())
            .hours("null")
            .build());

        //when
        mockMvc.perform(post("/users/{id}/favourites", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(f)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(f.restaurantId().toString()));

        verify(favouriteService).addFavourite(userId, f);
    }

    @Test
    @WithMockUser()
    void getFavourites_ReturnsAllUserFavouriteRestaurants() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        User u1 = User.builder().userId(UUID.randomUUID()).role(Role.ROLE_RESTAURANT).blocked(false).build();

        Address a1 = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();

        List<Restaurant> restaurants = Arrays.asList(
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).build(),
            Restaurant.builder().restaurantId(UUID.randomUUID()).user(u1).hours("null").address(a1).build()
        );

        given(favouriteService.getFavouritesByUserId(userId)).willReturn(restaurants);

        // when
        mockMvc.perform(get("/users/{id}/favourites", userId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(5));

        verify(favouriteService).getFavouritesByUserId(userId);
    }

    @Test
    @WithMockUser()
    void updateFavouritesOrder_UpdatesUserFavouriteRestaurantsOrder() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        List<RestaurantFavouriteInputDTO> favourites = Arrays.asList(
            new RestaurantFavouriteInputDTO(UUID.randomUUID(), 1),
            new RestaurantFavouriteInputDTO(UUID.randomUUID(), 2),
            new RestaurantFavouriteInputDTO(UUID.randomUUID(), 3)
        );

        //when
        mockMvc.perform(put("/users/{id}/favourites", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(favourites)))
            // then
            .andExpect(status().isOk());

        verify(favouriteService).updateFavourite(userId, favourites);
    }

    @Test
    @WithMockUser()
    void deleteFavourite_RemovesGivenRestaurantsFromUserFavourites() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        DeleteFavouriteRestaurantsDTO dfrd = new DeleteFavouriteRestaurantsDTO(
            userId,
            Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        );

        //when
        mockMvc.perform(post("/users/delete/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dfrd)))
            // then
            .andExpect(status().isNoContent());

        verify(favouriteService).deleteFavourite(dfrd);
    }
}
