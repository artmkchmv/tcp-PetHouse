package com.petshouse.petshouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.UserRepository;
import com.petshouse.petshouse.service.UserService;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        User registeredUser = userService.registerUser(user);

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getLogin()).isEqualTo("testuser");
        assertThat(registeredUser.getEmail()).isEqualTo("test@example.com");
        assertThat(registeredUser.getLocation()).isEqualTo("Moscow");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches("password123", registeredUser.getPassword()));
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        User savedUser = userRepository.save(user);

        User foundUser = userService.getUserById(savedUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getLogin()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testAuthenticateUser_Success() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        userService.registerUser(user);

        boolean isAuthenticated = userService.authenticateUser("testuser", "password123");

        assertTrue(isAuthenticated);
    }

    @Test
    public void testAuthenticateUser_Failure() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        userService.registerUser(user);

        boolean isAuthenticated = userService.authenticateUser("testuser", "wrongpassword");

        assertFalse(isAuthenticated);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        Long nonExistentUserId = 999L;
        
        assertThrows(RuntimeException.class, () -> userService.getUserById(nonExistentUserId));
    }
}
