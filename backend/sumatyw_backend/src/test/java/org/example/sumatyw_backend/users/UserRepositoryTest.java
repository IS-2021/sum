//package org.example.sumatyw_backend.users;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.testcontainers.containers.MariaDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class UserRepositoryTest {
//
//    @Container
//    @ServiceConnection
//    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @AfterEach
//    void tearDown() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void connectionTest(){
//        assertThat(mariaDB.isCreated()).isTrue();
//        assertThat(mariaDB.isRunning()).isTrue();
//    }
//
//    @Test
//    void findByUsername_ReturnsUserWithGivenUsername() {
//        // given
//        User u1 = User.builder().username("login1").build();
//        u1 = userRepository.save(u1);
//
//        User u2 = User.builder().username("login2").build();
//        u2 = userRepository.save(u2);
//
//        // when
//        Optional<User> foundUser = userRepository.findByUsername(u1.getUsername());
//
//        // then
//        assertThat(foundUser.isPresent()).isTrue();
//        assertThat(foundUser.get()).isEqualTo(u1);
//    }
//
//    @Test
//    void findByUsername_ReturnsEmptyOptional_IfUserWithGivenUsernameDoesNotExists() {
//        // given
//        User u1 = User.builder().username("login1").build();
//        u1 = userRepository.save(u1);
//
//        User u2 = User.builder().username("login2").build();
//        u2 = userRepository.save(u2);
//
//        // when
//        Optional<User> foundUser = userRepository.findByUsername("username");
//
//        // then
//        assertThat(foundUser.isPresent()).isFalse();
//    }
//
//    @Test
//    void findByPhoneNumber_ReturnsUserWithGivenPhoneNumber() {
//        // given
//        User u1 = User.builder().phoneNumber("111222333").build();
//        u1 = userRepository.save(u1);
//
//        User u2 = User.builder().phoneNumber("999888777").build();
//        u2 = userRepository.save(u2);
//
//        // when
//        Optional<User> foundUser = userRepository.findByPhoneNumber(u1.getPhoneNumber());
//
//        // then
//        assertThat(foundUser.isPresent()).isTrue();
//        assertThat(foundUser.get()).isEqualTo(u1);
//    }
//
//    @Test
//    void findByPhoneNumber_ReturnsEmptyOptional_IfUserWithGivenPhoneNumberDoesNotExists() {
//        // given
//        User u1 = User.builder().phoneNumber("111222333").build();
//        u1 = userRepository.save(u1);
//
//        User u2 = User.builder().phoneNumber("999888777").build();
//        u2 = userRepository.save(u2);
//
//        // when
//        Optional<User> foundUser = userRepository.findByPhoneNumber("123456789");
//
//        // then
//        assertThat(foundUser.isPresent()).isFalse();
//    }
//
//    @Test
//    void findByEmail_ReturnsUserWithGivenEmail() {
//        // given
//        User u1 = User.builder().email("mail1@mail.com").build();
//        u1 = userRepository.save(u1);
//
//        User u2 = User.builder().email("mail2@mail.com").build();
//        u2 = userRepository.save(u2);
//
//        // when
//        Optional<User> foundUser = userRepository.findByEmail(u1.getEmail());
//
//        // then
//        assertThat(foundUser.isPresent()).isTrue();
//        assertThat(foundUser.get()).isEqualTo(u1);
//    }
//
//    @Test
//    void findByEmail_ReturnsEmptyOptional_IfUserWithGivenEmailDoesNotExists() {
//        // given
//        User u1 = User.builder().email("mail1@mail.com").build();
//        u1 = userRepository.save(u1);
//
//        User u2 = User.builder().email("mail2@mail.com").build();
//        u2 = userRepository.save(u2);
//
//        // when
//        Optional<User> foundUser = userRepository.findByEmail("mmmmm@mail.com");
//
//        // then
//        assertThat(foundUser.isPresent()).isFalse();
//    }
//
//    @Test
//    void findByBlockedFalse_ReturnsOnlyNonBlockedUsers() {
//        // given
//        List<User> users = Arrays.asList(
//            User.builder().blocked(false).build(),
//            User.builder().blocked(false).build(),
//            User.builder().blocked(true).build()
//        );
//
//        userRepository.saveAll(users);
//
//        // when
//        List<User> nonBlockedUsers = userRepository.findByBlockedFalse();
//
//        // then
//        assertThat(nonBlockedUsers.size()).isEqualTo(2);
//
//        for (User u : nonBlockedUsers) {
//            assertThat(u.isBlocked()).isFalse();
//        }
//    }
//}
