package org.example.sumatyw_backend.users;

import com.google.maps.errors.ApiException;
import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.addresses.AddressService;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotAuthenticatedException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.example.sumatyw_backend.restaurants.RestaurantRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;

    public User addUser(User user) {

        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username: '" + user.getUsername() + "' already exists.");
        }

        if (this.userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("Phone number: '" + user.getPhoneNumber() + "' already exists.");
        }

        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email: '" + user.getPhoneNumber() + "' already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getNotBannedUsers() {
        return userRepository.findByBlockedFalse();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public void removeUserById(UUID id) {
        userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User not found with ID: " + id)
        );
        userRepository.deleteById(id);
    }

    public User getMeUser() throws UserNotAuthenticatedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotAuthenticatedException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UserNotFoundException("User not found with username: " + username)
            );

        return user;
    }

    public void banUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        user.setBlocked(true);
        userRepository.save(user);
    }

    public User updateUserById(UUID id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (this.userRepository.findByPhoneNumber(updatedUser.getPhoneNumber()).isPresent())
            throw new ResourceAlreadyExistsException("Phone number: '" + updatedUser.getPhoneNumber() + "' already exists.");

        if (this.userRepository.findByEmail(updatedUser.getEmail()).isPresent())
            throw new ResourceAlreadyExistsException("Email: '" + updatedUser.getPhoneNumber() + "' already exists.");

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setSecondName(updatedUser.getSecondName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    public void changePassword(UUID id, String password) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    public User updateUserAddress(UUID userId, String placeId) throws IOException, InterruptedException, ApiException {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        existingUser.setAddress(addressService.getAddress(placeId));

        return userRepository.save(existingUser);
    }
}
