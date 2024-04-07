package org.example.sumatyw_backend.users;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }
    public List<User> getUsers() {
        return  userRepository.findAll();
    }
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
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
        User existingUser = userRepository.findById(id).orElseThrow(RuntimeException::new);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setSecondName(updatedUser.getSecondName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setCity(updatedUser.getCity());
        return userRepository.save(existingUser);
    }

    public void activateAccount(UUID id) {
        User existingUser = userRepository.findById(id).orElseThrow(RuntimeException::new);
        existingUser.setActive(true);
        userRepository.save(existingUser);
    }

}
