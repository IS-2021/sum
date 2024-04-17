package org.example.sumatyw_backend.users;

import lombok.AllArgsConstructor;
import org.example.sumatyw_backend.cities.CityRepository;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;

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

        if (user.getRole().equals(Role.ROLE_RESTAURANT)) {
            user.setActive(false);
        }

        return userRepository.save(user);
    }
    public List<User> getUsers() {
        return  userRepository.findAll();
    }
    public User getUserById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
    public boolean removeUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return true;
        }
        return false;
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

        if (cityRepository.findByName(updatedUser.getCity().getName()).isEmpty())
            cityRepository.save(updatedUser.getCity());

        existingUser.setCity(updatedUser.getCity());

        return userRepository.save(existingUser);
    }

    public void changePassword(UUID id, String password) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
}
