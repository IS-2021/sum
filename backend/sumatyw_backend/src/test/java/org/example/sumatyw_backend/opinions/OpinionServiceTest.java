package org.example.sumatyw_backend.opinions;

import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.restaurants.RestaurantStatus;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {

    private OpinionService opinionService;
    @Mock
    private OpinionRepository opinionRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;

    Restaurant restaurant;
    User user;
    UUID userId;
    UUID restaurantId;

    @BeforeEach
    void setUp() {
        opinionService = new OpinionService(opinionRepository, restaurantRepository, userRepository);
        userId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        user = User.builder()
            .userId(userId)
            .username("testuser")
            .phoneNumber("1234567890")
            .email("test@example.com")
            .password("password")
            .blocked(false)
            .build();
        restaurant = Restaurant.builder()
            .restaurantId(restaurantId)
            .user(user)
            .name("Test Restaurant")
            .phoneNumber("123456789")
            .hours("{ \"monday\": [ \"10:00\", \"21:00\" ], \"tuesday\": [ \"08:00\", \"20:00\" ], \"wednesday\": [ \"10:00\", \"18:00\" ], \"thursday\": [ \"10:00\", \"20:00\" ], \"friday\": [ \"08:00\", \"23:00\" ], \"saturday\": [], \"sunday\": [ \"09:00\", \"22:00\" ] }")
            .status(RestaurantStatus.Inactive)
            .address(Address.builder().city("Test City").build())
            .build();

    }

    @Test
    void testAddOpinion_Success() {

        Opinion opinion = new Opinion(UUID.randomUUID(), user, restaurant, true, LocalDateTime.now());

        when(opinionRepository.findByUserUserIdAndRestaurantRestaurantId(userId, restaurantId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(opinionRepository.save(opinion)).thenReturn(opinion);

        Opinion savedOpinion = opinionService.addOpinion(opinion);

        assertNotNull(savedOpinion);
        assertEquals(opinion, savedOpinion);
        assertEquals(1, restaurant.getLikesCount());
    }

    @Test
    void testAddOpinion_UserNotFound() {
        Opinion opinion = new Opinion(UUID.randomUUID(), new User(), new Restaurant(), true, LocalDateTime.now());

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> opinionService.addOpinion(opinion));
    }

    @Test
    void testAddOpinion_RestaurantNotFound() {
        Opinion opinion = new Opinion(UUID.randomUUID(), new User(), new Restaurant(), true, LocalDateTime.now());

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> opinionService.addOpinion(opinion));
    }

//    @Test
//    void testAddOpinion_OpinionAlreadyExists() {
//        UUID userId = UUID.randomUUID();
//        UUID restaurantId = UUID.randomUUID();
//        User user = new User(userId, "username", "email", "password");
//        Restaurant restaurant = new Restaurant(restaurantId, user, "Restaurant", "123456789", null, null, RestaurantStatus.Active, null, null);
//
//        Opinion opinion = new Opinion(UUID.randomUUID(), user, restaurant, true, LocalDateTime.now());
//
//        when(opinionRepository.findByUserUserIdAndRestaurantRestaurantId(userId, restaurantId)).thenReturn(Optional.of(opinion));
//
//        assertThrows(ResourceAlreadyExistsException.class, () -> opinionService.addOpinion(opinion));
//    }


    @Test
    void getOpinions_ReturnsAllOpinions() {
        // given
        given(opinionRepository.findAll()).willReturn(new ArrayList<>());

        // when
        opinionService.getOpinions();

        // then
        verify(opinionRepository).findAll();
    }

    @Test
    void updateOpinionById_ChangesNegativeOpinionToPositive() {
        // given
        UUID opinionId = UUID.randomUUID();
        int likesCount = 10;
        int dislikesCount = 10;
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).likesCount(likesCount).dislikesCount(dislikesCount).build();
        Opinion opinionDB = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();
        Opinion requestOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(true).build();

        given(opinionRepository.findById(opinionId)).willReturn(Optional.of(opinionDB));
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.of(restaurant));
        given(restaurantRepository.save(restaurant)).willReturn(restaurant);
        given(opinionRepository.save(opinionDB)).willReturn(opinionDB);

        // when
        opinionService.updateOpinionById(opinionId, requestOpinion);

        // then
        ArgumentCaptor<UUID> opinionIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        ArgumentCaptor<Opinion> opinionCaptor = ArgumentCaptor.forClass(Opinion.class);

        verify(opinionRepository).findById(opinionIdCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());
        verify(restaurantRepository).save(restaurantCaptor.capture());
        verify(opinionRepository).save(opinionCaptor.capture());

        UUID capturedOpinionId = opinionIdCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();
        Restaurant capturedRestaurant = restaurantCaptor.getValue();
        Opinion capturedOpinion = opinionCaptor.getValue();

        assertThat(capturedOpinionId).isEqualTo(opinionId);
        assertThat(capturedUserId).isEqualTo(opinionDB.getUser().getUserId());
        assertThat(capturedRestaurantId).isEqualTo(opinionDB.getRestaurant().getRestaurantId());
        assertThat(capturedRestaurant.getLikesCount()).isEqualTo(likesCount + 1);
        assertThat(capturedRestaurant.getDislikesCount()).isEqualTo(dislikesCount - 1);
        assertThat(capturedOpinion.isPositive()).isTrue();
    }

    @Test
    void updateOpinionById_ChangesPositiveOpinionToNegative() {
        // given
        UUID opinionId = UUID.randomUUID();
        int likesCount = 10;
        int dislikesCount = 10;
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).likesCount(likesCount).dislikesCount(dislikesCount).build();
        Opinion opinionDB = Opinion.builder().user(user).restaurant(restaurant).isPositive(true).build();
        Opinion requestOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();

        given(opinionRepository.findById(opinionId)).willReturn(Optional.of(opinionDB));
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.of(restaurant));
        given(restaurantRepository.save(restaurant)).willReturn(restaurant);
        given(opinionRepository.save(opinionDB)).willReturn(opinionDB);

        // when
        opinionService.updateOpinionById(opinionId, requestOpinion);

        // then
        ArgumentCaptor<UUID> opinionIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        ArgumentCaptor<Opinion> opinionCaptor = ArgumentCaptor.forClass(Opinion.class);

        verify(opinionRepository).findById(opinionIdCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());
        verify(restaurantRepository).save(restaurantCaptor.capture());
        verify(opinionRepository).save(opinionCaptor.capture());

        UUID capturedOpinionId = opinionIdCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();
        Restaurant capturedRestaurant = restaurantCaptor.getValue();
        Opinion capturedOpinion = opinionCaptor.getValue();

        assertThat(capturedOpinionId).isEqualTo(opinionId);
        assertThat(capturedUserId).isEqualTo(opinionDB.getUser().getUserId());
        assertThat(capturedRestaurantId).isEqualTo(opinionDB.getRestaurant().getRestaurantId());
        assertThat(capturedRestaurant.getLikesCount()).isEqualTo(likesCount - 1);
        assertThat(capturedRestaurant.getDislikesCount()).isEqualTo(dislikesCount + 1);
        assertThat(capturedOpinion.isPositive()).isFalse();
    }

    @Test
    void updateOpinionById_ThrowsObjectNotFoundException_IfOpinionWithGivenIdDoesNotExist() {
        // given
        UUID opinionId = UUID.randomUUID();

        given(opinionRepository.findById(opinionId)).willReturn(Optional.empty());

        // when

        // then
        ArgumentCaptor<UUID> opinionIdCaptor = ArgumentCaptor.forClass(UUID.class);

        assertThatThrownBy(() -> opinionService.updateOpinionById(opinionId, any()))
            .isInstanceOf(ObjectNotFoundException.class);

        verify(opinionRepository).findById(opinionIdCaptor.capture());

        UUID capturedOpinionId = opinionIdCaptor.getValue();

        assertThat(capturedOpinionId).isEqualTo(opinionId);
    }

    @Test
    void updateOpinionById_ThrowsObjectNotFoundException_IfUserWithGivenIdDoesNotExist() {
        // given
        UUID opinionId = UUID.randomUUID();
        int likesCount = 10;
        int dislikesCount = 10;
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).likesCount(likesCount).dislikesCount(dislikesCount).build();
        Opinion opinionDB = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();
        Opinion requestOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(true).build();

        given(opinionRepository.findById(opinionId)).willReturn(Optional.of(opinionDB));
        given(userRepository.findById(user.getUserId())).willReturn(Optional.empty());

        // when

        // then
        ArgumentCaptor<UUID> opinionIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);

        assertThatThrownBy(() -> opinionService.updateOpinionById(opinionId, requestOpinion))
            .isInstanceOf(ObjectNotFoundException.class);

        verify(opinionRepository).findById(opinionIdCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());

        UUID capturedOpinionId = opinionIdCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();

        assertThat(capturedOpinionId).isEqualTo(opinionId);
        assertThat(capturedUserId).isEqualTo(opinionDB.getUser().getUserId());
    }

    @Test
    void updateOpinionById_ThrowsObjectNotFoundException_IfRestaurantWithGivenIdDoesNotExist() {
        // given
        UUID opinionId = UUID.randomUUID();
        int likesCount = 10;
        int dislikesCount = 10;
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).likesCount(likesCount).dislikesCount(dislikesCount).build();
        Opinion opinionDB = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();
        Opinion requestOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(true).build();

        given(opinionRepository.findById(opinionId)).willReturn(Optional.of(opinionDB));
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.empty());

        // when

        // then
        ArgumentCaptor<UUID> opinionIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);

        assertThatThrownBy(() -> opinionService.updateOpinionById(opinionId, requestOpinion))
            .isInstanceOf(ObjectNotFoundException.class);

        verify(opinionRepository).findById(opinionIdCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());

        UUID capturedOpinionId = opinionIdCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();

        assertThat(capturedOpinionId).isEqualTo(opinionId);
        assertThat(capturedUserId).isEqualTo(opinionDB.getUser().getUserId());
        assertThat(capturedRestaurantId).isEqualTo(opinionDB.getRestaurant().getRestaurantId());
    }
}
