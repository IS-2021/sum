package org.example.sumatyw_backend.users;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;
import java.util.UUID;

import com.google.maps.errors.ApiException;
import org.example.sumatyw_backend.addresses.Address;
import org.example.sumatyw_backend.addresses.AddressService;
import org.example.sumatyw_backend.exceptions.ResourceAlreadyExistsException;
import org.example.sumatyw_backend.exceptions.UserNotAuthenticatedException;
import org.example.sumatyw_backend.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UUID userId;
    private User user;
    private Address address;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        user = User.builder()
            .userId(userId)
            .username("testuser")
            .phoneNumber("1234567890")
            .email("test@example.com")
            .password("password")
            .blocked(false)
            .build();

         address = Address.builder()
            .addressId("place_id_123")
            .city("New York")
            .street("Broadway")
            .number("123")
            .postalCode("10001")
            .country("USA")
            .latitude(40.7128)
            .longitude(-74.0060)
            .build();
    }

    @Test
    void testAddUser_Success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.addUser(user);

        assertEquals(user, result);
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAddUser_UsernameExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Username: '" + user.getUsername() + "' already exists.", exception.getMessage());
    }

    @Test
    void testAddUser_PhoneNumberExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Phone number: '" + user.getPhoneNumber() + "' already exists.", exception.getMessage());
    }

    @Test
    void testAddUser_EmailExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Email: '" + user.getPhoneNumber() + "' already exists.", exception.getMessage());
    }

    @Test
    void testGetUsers_Success() {
        List<User> userList = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getUsers();

        assertEquals(userList, result);
    }

    @Test
    void testGetNotBannedUsers_Success() {
        List<User> userList = Arrays.asList(user);
        when(userRepository.findByBlockedFalse()).thenReturn(userList);

        List<User> result = userService.getNotBannedUsers();

        assertEquals(userList, result);
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(user, result);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail(user.getEmail());

        assertEquals(user, result);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail(user.getEmail());
        });

        assertEquals("User not found with email: " + user.getEmail(), exception.getMessage());
    }

    @Test
    void testRemoveUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.removeUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void testRemoveUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.removeUserById(userId);
        });

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    void testGetMeUser_UserNotFound() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getMeUser();
        });

        assertEquals("User not found with username: testuser", exception.getMessage());
    }

    @Test
    void testGetMeUser_Success() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.getMeUser();

        assertEquals(user, result);
    }

    @Test
    void testGetMeUser_UserNotAuthenticated() {
        AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken(
            "key", "anonymous", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exception exception = assertThrows(UserNotAuthenticatedException.class, () -> {
            userService.getMeUser();
        });

        assertEquals("User is not authenticated", exception.getMessage());
    }

    @Test
    void testBanUserById_Success() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
            .userId(userId)
            .blocked(false)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.banUserById(userId);

        assertTrue(user.isBlocked());
    }

    @Test
    void testBanUserById_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.banUserById(userId);
        });
    }

    @Test
    void testUpdateUserById_Success() {
        UUID userId = UUID.randomUUID();
        User existingUser = User.builder()
            .userId(userId)
            .firstName("John")
            .secondName("Doe")
            .email("john.doe@example.com")
            .phoneNumber("1234567890")
            .build();

        User updatedUser = User.builder()
            .userId(userId)
            .firstName("Updated John")
            .secondName("Updated Doe")
            .email("updated.doe@example.com")
            .phoneNumber("0987654321")
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByPhoneNumber(updatedUser.getPhoneNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUserById(userId, updatedUser);

        assertEquals(updatedUser, result);
    }

    @Test
    void testUpdateUserById_UserNotFound() {
        UUID userId = UUID.randomUUID();
        User updatedUser = User.builder()
            .userId(userId)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserById(userId, updatedUser);
        });
    }

    @Test
    void testUpdateUserById_PhoneNumberExists() {
        UUID userId = UUID.randomUUID();
        User existingUser = User.builder()
            .userId(userId)
            .phoneNumber("1234567890")
            .build();

        User updatedUser = User.builder()
            .userId(userId)
            .phoneNumber("1234567890")
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByPhoneNumber(updatedUser.getPhoneNumber())).thenReturn(Optional.of(existingUser));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.updateUserById(userId, updatedUser);
        });
    }

    @Test
    void testUpdateUserById_EmailExists() {
        UUID userId = UUID.randomUUID();
        User existingUser = User.builder()
            .userId(userId)
            .email("john.doe@example.com")
            .build();

        User updatedUser = User.builder()
            .userId(userId)
            .email("john.doe@example.com")
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.updateUserById(userId, updatedUser);
        });
    }

    @Test
    void testChangePassword_Success() {
        UUID userId = UUID.randomUUID();
        String newPassword = "newPassword";
        User user = User.builder()
            .userId(userId)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        userService.changePassword(userId, newPassword);

        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testChangePassword_UserNotFound() {
        UUID userId = UUID.randomUUID();
        String newPassword = "newPassword";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.changePassword(userId, newPassword);
        });
    }

    @Test
    void testUpdateUserAddress_Success() throws IOException, InterruptedException, ApiException {
        UUID userId = user.getUserId();
        String placeId = "placeId";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressService.getAddress(placeId)).thenReturn(address);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUserAddress(userId, placeId);

        verify(userRepository).save(user);
        assertEquals(address, result.getAddress().get());
    }

    @Test
    void testUpdateUserAddress_UserNotFound() throws IOException, InterruptedException, ApiException {
        UUID userId = UUID.randomUUID();
        String placeId = "placeId";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserAddress(userId, placeId);
        });
    }
}


