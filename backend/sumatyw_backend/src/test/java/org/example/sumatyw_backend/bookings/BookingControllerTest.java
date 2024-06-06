package org.example.sumatyw_backend.bookings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.meals.Meal;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantStatus;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void createBooking_AddsNewBooking() throws Exception{
        // given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        BookingInputDTO bookingInputDTO = new BookingInputDTO(m.getMealId(), userId);
        Booking b = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m)
            .timestamp(LocalDateTime.now())
            .build();

        given(bookingService.createBooking(any())).willReturn(b);

        // when
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingInputDTO)))
            // then
            .andExpect(status().isOk());

        verify(bookingService).createBooking(any());
    }

    @Test
    void getAllBookings_ReturnsAllActiveBookings() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();
        Meal m2 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b2 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m2)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();

        given(bookingService.getAllActiveBookings()).willReturn(Arrays.asList(b1, b2));

        // when
        mockMvc.perform(get("/bookings"))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].status").value(Status.Active.toString()))
            .andExpect(jsonPath("$[1].status").value(Status.Active.toString()));

        verify(bookingService).getAllActiveBookings();
    }

    @Test
    void getBookingById_ReturnsBookingWithGivenId() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .build();

        given(bookingService.getBookingById(b1.getBookedId())).willReturn(b1);

        // when
        mockMvc.perform(get("/bookings/{id}", b1.getBookedId()))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookingId").value(b1.getBookedId().toString()));

        verify(bookingService).getBookingById(b1.getBookedId());
    }

    @Test
    void getBookingByUserId_ReturnsActiveBookingOfGivenUser() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();

        given(bookingService.getBookingByUserId(userId)).willReturn(b1);

        // when
        mockMvc.perform(get("/bookings/active")
                .param("userId", userId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Status.Active.toString()));

        verify(bookingService).getBookingByUserId(userId);
    }

    @Test
    void getAllBookingsByUserId_ReturnsAllUserBookings() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();
        Meal m2 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b2 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m2)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();

        given(bookingService.getAllUserBookings(userId)).willReturn(Arrays.asList(b1, b2));

        // when
        mockMvc.perform(get("/bookings")
                .param("userId", userId.toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].userId").value(userId.toString()))
            .andExpect(jsonPath("$[1].userId").value(userId.toString()));


        verify(bookingService).getAllUserBookings(userId);
    }

    @Test
    void getBookingsByRestaurantId_ReturnsAllRestaurantBookings() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();
        Meal m2 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b2 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m2)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();

        given(bookingService.getBookingsByRestaurantID(r.getRestaurantId())).willReturn(Arrays.asList(b1, b2));

        // when
        mockMvc.perform(get("/bookings")
                .param("restaurantId", r.getRestaurantId().toString()))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].restaurant.id").value(r.getRestaurantId().toString()))
            .andExpect(jsonPath("$[1].restaurant.id").value(r.getRestaurantId().toString()));


        verify(bookingService).getBookingsByRestaurantID(r.getRestaurantId());
    }

    @Test
    void getActiveBookingsByRestaurantId_ReturnsAllActiveRestaurantBooking_IfActiveParamIsTrue() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();
        Meal m2 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b2 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m2)
            .timestamp(LocalDateTime.now())
            .status(Status.Active)
            .build();

        given(bookingService.getActiveBookingsByRestaurantID(r.getRestaurantId(), true)).willReturn(Arrays.asList(b1, b2));

        // when
        mockMvc.perform(get("/bookings")
                .param("restaurantId", r.getRestaurantId().toString())
                .param("active", "true"))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].restaurant.id").value(r.getRestaurantId().toString()))
            .andExpect(jsonPath("$[0].status").value(Status.Active.toString()))
            .andExpect(jsonPath("$[1].restaurant.id").value(r.getRestaurantId().toString()))
            .andExpect(jsonPath("$[1].status").value(Status.Active.toString()));

        verify(bookingService).getActiveBookingsByRestaurantID(r.getRestaurantId(), true);
    }

    @Test
    void getActiveBookingsByRestaurantId_ReturnsAllNonActiveRestaurantBooking_IfActiveParamIsFalse() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.OutOfDate)
            .build();
        Meal m2 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b2 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m2)
            .timestamp(LocalDateTime.now())
            .status(Status.PickedUp)
            .build();

        given(bookingService.getActiveBookingsByRestaurantID(r.getRestaurantId(), false)).willReturn(Arrays.asList(b1, b2));

        // when
        mockMvc.perform(get("/bookings")
                .param("restaurantId", r.getRestaurantId().toString())
                .param("active", "false"))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].restaurant.id").value(r.getRestaurantId().toString()))
            .andExpect(jsonPath("$[0].status").value(Status.OutOfDate.toString()))
            .andExpect(jsonPath("$[1].restaurant.id").value(r.getRestaurantId().toString()))
            .andExpect(jsonPath("$[1].status").value(Status.PickedUp.toString()));

        verify(bookingService).getActiveBookingsByRestaurantID(r.getRestaurantId(), false);
    }

    @Test
    void markBookingAsPickedUp_UpdatesBookingStatusToPickedUp() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.PickedUp)
            .build();

        BookingInputDTO bookingInputDTO = new BookingInputDTO(m1.getMealId(), userId);

        given(bookingService.markBookingAsPickedUp(any(), any())).willReturn(b1);

        // when
        mockMvc.perform(put("/bookings/{id}", b1.getBookedId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingInputDTO)))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Status.PickedUp.toString()));

        verify(bookingService).markBookingAsPickedUp(any(), any());
    }

    @Test
    void cancelBookingById_UpdatesBookingStatusToCancelled() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        Address a = Address.builder().addressId("asdfasdfadfads").city("Lodz").build();
        Restaurant r = Restaurant.builder()
            .restaurantId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .hours("null").address(a)
            .status(RestaurantStatus.Active)
            .build();
        Meal m1 = Meal.builder().mealId(UUID.randomUUID()).restaurant(r).build();
        Booking b1 = Booking.builder()
            .bookedId(UUID.randomUUID())
            .user(User.builder().userId(userId).build())
            .meal(m1)
            .timestamp(LocalDateTime.now())
            .status(Status.Cancelled)
            .build();

        given(bookingService.cancelBookingById(b1.getBookedId())).willReturn(b1);

        // when
        mockMvc.perform(delete("/bookings/{id}", b1.getBookedId()))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Status.Cancelled.toString()));

        verify(bookingService).cancelBookingById(b1.getBookedId());
    }
}
