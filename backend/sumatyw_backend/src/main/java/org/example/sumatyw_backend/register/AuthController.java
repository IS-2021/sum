package org.example.sumatyw_backend.register;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.users.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> handleRegister(@Valid @RequestBody UserInputDTO userInputDTO) {

        User user = userService.addUser(UserDTOMapper.mapUserInputDTOToUser(userInputDTO));

        return new ResponseEntity<>(
            UserDTOMapper.mapUserToUserDTO(user),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/change-password")
    private ResponseEntity changePassword(@PathVariable("id") UUID id, @RequestBody PasswordDTO passwordDTO) {

        userService.changePassword(id, passwordDTO.getPassword());

        return new ResponseEntity<>(
            HttpStatus.OK
        );
    }


//    public static boolean isValidPassword(String password) {
//        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
//
//        Pattern pattern = Pattern.compile(regex);
//
//        Matcher matcher = pattern.matcher(password);
//
//        return matcher.matches();
//    }
}
