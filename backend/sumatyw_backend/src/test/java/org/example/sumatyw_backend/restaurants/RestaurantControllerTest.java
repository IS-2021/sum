package org.example.sumatyw_backend.restaurants;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressDTO;
import org.example.sumatyw_backend.addresses.AddressInputDTO;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void testGetRestaurants() throws Exception {
        // given
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantId(UUID.randomUUID());
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setRestaurantId(UUID.randomUUID());
        Address address = new Address();
        Address address2 = new Address();
        restaurant1.setAddress(address);
        restaurant2.setAddress(address2);
        User user = new User();
        user.setUserId(UUID.randomUUID());
        restaurant1.setUser(user);
        User user2 = new User();
        user.setUserId(UUID.randomUUID());
        restaurant2.setUser(user2);

        restaurant1.setHours("null");
        restaurant2.setHours("null");
        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        // when // then
        mockMvc.perform(get("/restaurants"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(restaurants.size()));

        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    public void testGetRestaurantById() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        Address address = new Address();
        restaurant.setAddress(address);
        User user = new User();
        user.setUserId(UUID.randomUUID());
        restaurant.setUser(user);
        restaurant.setHours("null");
        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(restaurant);

        // when // then
        mockMvc.perform(get("/restaurants/{id}", restaurantId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restaurantId.toString()));

        verify(restaurantService, times(1)).getRestaurantById(restaurantId);
    }

    @Test
    public void testDeleteRestaurantById() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        doNothing().when(restaurantService).removeRestaurantById(restaurantId);

        // when // then
        mockMvc.perform(delete("/restaurants/{id}", restaurantId))
            .andExpect(status().isNoContent());

        verify(restaurantService, times(1)).removeRestaurantById(restaurantId);
    }

    @Test
    public void testUpdateRestaurantById() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        RestaurantInputDTO restaurantInputDTO = new RestaurantInputDTO("name", "1234567890", new AddressInputDTO("1", "12", "piotrkowska", "94-504", "lodz", "polska", 5d, 5d), UUID.randomUUID(), new Hours());
        Restaurant restaurant = RestaurantDTOMapper.mapRestaurantInputDTOToRestaurant(restaurantInputDTO);
        restaurant.setRestaurantId(restaurantId);

        when(restaurantService.updateRestaurantById(eq(restaurantId), any(Restaurant.class))).thenReturn(restaurant);

        // when // then
        mockMvc.perform(put("/restaurants/{id}", restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantInputDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restaurantId.toString()));

        verify(restaurantService, times(1)).updateRestaurantById(eq(restaurantId), any(Restaurant.class));
    }

    @Test
    public void testAddRestaurant() throws Exception {
        // given
        RestaurantInputDTO restaurantInputDTO = new RestaurantInputDTO(
            "name",
            "1234567890",
            new AddressInputDTO("1", "12", "piotrkowska", "94-504", "lodz", "polska", 5d, 5d),
            UUID.randomUUID(),
            new Hours()
        );

        Restaurant restaurant = RestaurantDTOMapper.mapRestaurantInputDTOToRestaurant(restaurantInputDTO);
        UUID restaurantId = UUID.randomUUID();
        restaurant.setRestaurantId(restaurantId);
        RestaurantDTO restaurantDTO = RestaurantDTOMapper.mapRestaurantToRestaurantDTO(restaurant);

        when(restaurantService.addRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        // when // then
        mockMvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantInputDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restaurantId.toString()));

        verify(restaurantService, times(1)).addRestaurant(any(Restaurant.class));
    }


    @Test
    public void testGetRestaurantsByCity() throws Exception {
        // given
        String city = "TestCity";
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantId(UUID.randomUUID());
        Address address = new Address();
        restaurant1.setAddress(address);
        User user = new User();
        user.setUserId(UUID.randomUUID());
        restaurant1.setUser(user);
        restaurant1.setHours("null");
        List<Restaurant> restaurants = Arrays.asList(restaurant1);
        when(restaurantService.getRestaurantsByCity(city)).thenReturn(restaurants);

        // when // then
        mockMvc.perform(get("/restaurants/city/{cityName}", city))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(restaurants.size()));

        verify(restaurantService, times(1)).getRestaurantsByCity(city);
    }

    @Test
    public void testDeactivateRestaurantById() throws Exception {
        // given
        UUID restaurantId = UUID.randomUUID();
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantId, "name", "1234567890", UUID.randomUUID(), new AddressDTO("1", "12", "piotrkowska", "94-504", "lodz", "polska", 5d, 5d), new Hours(), "imageUrl", 0, 0, RestaurantStatus.Inactive);
        when(restaurantService.deactivateRestaurant(restaurantId)).thenReturn(restaurantDTO);

        // when // then
        mockMvc.perform(put("/restaurants/deactivate/{id}", restaurantId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restaurantId.toString()));

        verify(restaurantService, times(1)).deactivateRestaurant(restaurantId);
    }

    @Test
    public void testGetLocalRestaurants() throws Exception {
        // given
        double lat = 12.34;
        double lon = 56.78;
        double radius = 10.0;
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantId(UUID.randomUUID());
        Address address = new Address();
        restaurant1.setAddress(address);
        User user = new User();
        user.setUserId(UUID.randomUUID());
        restaurant1.setUser(user);
        restaurant1.setHours("null");
        List<Restaurant> restaurants = Arrays.asList(restaurant1);
        when(restaurantService.getLocalRestaurants(lat, lon, radius)).thenReturn(restaurants);

        // when // then
        mockMvc.perform(get("/restaurants")
                .param("lat", String.valueOf(lat))
                .param("lon", String.valueOf(lon))
                .param("radius", String.valueOf(radius)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(restaurants.size()));

        verify(restaurantService, times(1)).getLocalRestaurants(lat, lon, radius);
    }
}
