package org.example.sumatyw_backend.opinions;

import org.example.sumatyw_backend.exceptions.ObjectNotFoundException;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.restaurants.Restaurant;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OpinionServiceTest {

    private OpinionService opinionService;
    @Mock
    private OpinionRepository opinionRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        opinionService = new OpinionService(opinionRepository, restaurantRepository, userRepository);
    }

    @Test
    void addOpinion_AddsNewPositiveOpinion() {
        // given
        int likesCount = 10;
        int dislikesCount = 10;
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).likesCount(likesCount).dislikesCount(dislikesCount).build();
        Opinion positiveOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(true).build();

        given(opinionRepository.findAllByUser(any())).willReturn(new ArrayList<>());
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.of(restaurant));
        given(opinionRepository.save(positiveOpinion)).willReturn(positiveOpinion);

        // when
        Opinion savedOpinion = opinionService.addOpinion(positiveOpinion);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        ArgumentCaptor<Opinion> opinionCaptor = ArgumentCaptor.forClass(Opinion.class);

        verify(opinionRepository).findAllByUser(userCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());
        verify(restaurantRepository).save(restaurantCaptor.capture());
        verify(opinionRepository).save(opinionCaptor.capture());

        User capturedUser = userCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();
        Restaurant capturedRestaurant = restaurantCaptor.getValue();
        Opinion capturedOpinion = opinionCaptor.getValue();

        assertThat(capturedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(capturedUserId).isEqualTo(user.getUserId());
        assertThat(capturedRestaurantId).isEqualTo(restaurant.getRestaurantId());
        assertThat(capturedRestaurant.getLikesCount()).isEqualTo(likesCount+1);
        assertThat(capturedRestaurant.getDislikesCount()).isEqualTo(dislikesCount);
        assertThat(capturedOpinion).isEqualTo(savedOpinion);
    }

    @Test
    void addOpinion_AddsNewNegativeOpinion() {
        // given
        int likesCount = 10;
        int dislikesCount = 10;
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).likesCount(likesCount).dislikesCount(dislikesCount).build();
        Opinion negativeOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();

        given(opinionRepository.findAllByUser(any())).willReturn(new ArrayList<>());
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.of(restaurant));
        given(opinionRepository.save(negativeOpinion)).willReturn(negativeOpinion);

        // when
        Opinion savedOpinion = opinionService.addOpinion(negativeOpinion);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        ArgumentCaptor<Opinion> opinionCaptor = ArgumentCaptor.forClass(Opinion.class);

        verify(opinionRepository).findAllByUser(userCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());
        verify(restaurantRepository).save(restaurantCaptor.capture());
        verify(opinionRepository).save(opinionCaptor.capture());

        User capturedUser = userCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();
        Restaurant capturedRestaurant = restaurantCaptor.getValue();
        Opinion capturedOpinion = opinionCaptor.getValue();

        assertThat(capturedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(capturedUserId).isEqualTo(user.getUserId());
        assertThat(capturedRestaurantId).isEqualTo(restaurant.getRestaurantId());
        assertThat(capturedRestaurant.getLikesCount()).isEqualTo(likesCount);
        assertThat(capturedRestaurant.getDislikesCount()).isEqualTo(dislikesCount+1);
        assertThat(capturedOpinion).isEqualTo(savedOpinion);
    }

    @Test
    void addOpinion_WillThrowObjectNotFoundException_IfUserWithGivenIdDoesNotExist() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        Opinion negativeOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();

        given(opinionRepository.findAllByUser(any())).willReturn(new ArrayList<>());
        given(userRepository.findById(user.getUserId())).willReturn(Optional.empty());

        // when

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);

        assertThatThrownBy(() -> opinionService.addOpinion(negativeOpinion))
            .isInstanceOf(ObjectNotFoundException.class);

        verify(opinionRepository).findAllByUser(userCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository, never()).findById(any());
        verify(restaurantRepository, never()).save(any());
        verify(opinionRepository, never()).save(any());

        User capturedUser = userCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();

        assertThat(capturedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(capturedUserId).isEqualTo(user.getUserId());
    }

    @Test
    void addOpinion_WillThrowObjectNotFoundException_IfRestaurantWithGivenIdDoesNotExist() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        Opinion negativeOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();

        given(opinionRepository.findAllByUser(any())).willReturn(new ArrayList<>());
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.empty());

        // when

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);

        assertThatThrownBy(() -> opinionService.addOpinion(negativeOpinion))
            .isInstanceOf(ObjectNotFoundException.class);

        verify(opinionRepository).findAllByUser(userCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());

        User capturedUser = userCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();

        assertThat(capturedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(capturedUserId).isEqualTo(user.getUserId());
        assertThat(capturedRestaurantId).isEqualTo(restaurant.getRestaurantId());
    }

    @Test
    void addOpinion_WillThrowResourceAlreadyExistsException_IfUserAlreadySentOpinionAboutGivenRestaurant() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Restaurant restaurant = Restaurant.builder().restaurantId(UUID.randomUUID()).build();
        Opinion negativeOpinion = Opinion.builder().user(user).restaurant(restaurant).isPositive(false).build();
        List<Opinion> userOpinions = new ArrayList<>();
        userOpinions.add(Opinion.builder().restaurant(Restaurant.builder().restaurantId(UUID.randomUUID()).build()).build());
        userOpinions.add(negativeOpinion);

        given(opinionRepository.findAllByUser(any())).willReturn(userOpinions);
        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(restaurantRepository.findById(restaurant.getRestaurantId())).willReturn(Optional.of(restaurant));

        // when

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UUID> userIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<UUID> restaurantIdCaptor = ArgumentCaptor.forClass(UUID.class);

        assertThatThrownBy(() -> opinionService.addOpinion(negativeOpinion))
            .isInstanceOf(ResourceAlreadyExistsException.class);

        verify(opinionRepository).findAllByUser(userCaptor.capture());
        verify(userRepository).findById(userIdCaptor.capture());
        verify(restaurantRepository).findById(restaurantIdCaptor.capture());

        User capturedUser = userCaptor.getValue();
        UUID capturedUserId = userIdCaptor.getValue();
        UUID capturedRestaurantId = restaurantIdCaptor.getValue();

        assertThat(capturedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(capturedUserId).isEqualTo(user.getUserId());
        assertThat(capturedRestaurantId).isEqualTo(restaurant.getRestaurantId());
    }

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
