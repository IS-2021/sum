package org.example.sumatyw_backend.register;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.cities.City;
import org.example.sumatyw_backend.cities.CityRepository;
import org.example.sumatyw_backend.users.User;
import org.example.sumatyw_backend.users.UserInputDTO;
import org.example.sumatyw_backend.users.UserDTOMapper;
import org.example.sumatyw_backend.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> handleRegister(@Valid @RequestBody UserInputDTO userInputDTO) {
        User newUser = UserDTOMapper.mapUserInputDTOToUser(userInputDTO);

        if (this.userRepository.findByUsername(userInputDTO.username()).isPresent()) {
            return new ResponseEntity<>(
                Map.of("message", "User with given username already exists."),
                HttpStatus.BAD_REQUEST
            );
        }

        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setRestaurant(userInputDTO.restaurant());



        if (userInputDTO.restaurant() != null) {
            newUser.setActive(false);
            Optional<City> city = cityRepository.findByName(userInputDTO.restaurant().getAddress().getCity().getName());
            if (city.isEmpty()) {
                cityRepository.save(userInputDTO.restaurant().getAddress().getCity());
            } else {
                newUser.getRestaurant().getAddress().setCity(city.get());
            }
        }



        newUser =  userRepository.save(newUser);

        return new ResponseEntity<>(
            Map.of("userId", newUser.getUserId().toString()),
            HttpStatus.CREATED
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
