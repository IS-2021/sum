package org.example.sumatyw_backend.users;

import org.example.sumatyw_backend.cities.CityDTOMapper;

public class UserDTOMapper {

    public static User mapUserInputDTOToUser(UserInputDTO userInputDTO) {
        return User.builder()
            .firstName(userInputDTO.firstName())
            .secondName(userInputDTO.secondName())
            .username(userInputDTO.username())
            .password(userInputDTO.password())
            .email(userInputDTO.email())
            .phoneNumber(userInputDTO.phoneNumber())
            .role(userInputDTO.role())
            .blocked(false)
            .build();
    }

    public static UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(
            user.getUserId().toString(),
            user.getFirstName(),
            user.getSecondName(),
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getRole(),
            user.isBlocked()
        );
    }

    public static  UserMeDTO mapUserToUserMeDTO(User user) {
        return new UserMeDTO(
            user.getUserId().toString(),
            user.getFirstName(),
            user.getSecondName(),
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getRole(),
            user.getCity() == null ? null : CityDTOMapper.mapCityToCityDTO(user.getCity())
        );
    }
}
