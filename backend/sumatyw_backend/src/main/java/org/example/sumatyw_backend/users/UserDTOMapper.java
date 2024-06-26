package org.example.sumatyw_backend.users;

import org.example.sumatyw_backend.addresses.AddressDTO;
import org.example.sumatyw_backend.addresses.AddressDTOMapper;

import java.util.Optional;

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

    public static UserMeDTO mapUserToUserMeDTO(User user) {
        Optional<AddressDTO> addressDTO = user.getAddress()
            .map(AddressDTOMapper::mapAddressToAddressDTO);

        return new UserMeDTO(
            user.getUserId().toString(),
            user.getFirstName(),
            user.getSecondName(),
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getRole(),
            addressDTO
        );
    }
}
