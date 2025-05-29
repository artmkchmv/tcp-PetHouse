package com.petshouse.petshouse;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.service.UserService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetUserById() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        User foundUser = userService.getUserById(savedUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getLogin()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getLocation()).isEqualTo("Moscow");
    }

    @Test
    public void testGetUserByLogin() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        User foundUser = userService.getUserByLogin(savedUser.getLogin());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getLogin()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getLocation()).isEqualTo("Moscow");
    }

    @Test
    public void testAuthenticateUser_Success() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        userService.saveUser(user);
        boolean isAuthenticated = userService.authenticateUser("testuser", "password123");
        assertTrue(isAuthenticated);
    }

    @Test
    public void testAuthenticateUser_Failure() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        userService.saveUser(user);
        boolean isAuthenticated = userService.authenticateUser("testuser", "wrongpassword");
        assertFalse(isAuthenticated);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        Long nonExistentUserId = 999L;
        assertThrows(RuntimeException.class, () -> userService.getUserById(nonExistentUserId));
    }

    @Test
    public void testUpdateUserPassword() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        User updatedUser = userService.updateUserPassword(savedUser.getId(), "newPassword123");
        assertThat(updatedUser).isNotNull();
        assertThat(passwordEncoder.matches("newPassword123", updatedUser.getPassword())).isTrue();
    }

    @Test
    public void testUpdateUserPasswordWithNullValue() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        
        assertThrows(BadRequestException.class, () -> {
            userService.updateUserPassword(savedUser.getId(), null);
        });
    }

    @Test
    public void testUpdateUserEmail() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        User updatedUser = userService.updateUserEmail(savedUser.getId(), "newemail@example.com");
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("newemail@example.com");
    }

    @Test
    public void testUpdateUserEmailWithBlankValue() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        
        assertThrows(BadRequestException.class, () -> {
            userService.updateUserEmail(savedUser.getId(), "");
        });
    }

    @Test
    public void testUpdateUserLocation() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        User updatedUser = userService.updateUserLocation(savedUser.getId(), "Saint Petersburg");
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getLocation()).isEqualTo("Saint Petersburg");
    }

    @Test
    public void testUpdateUserLocationWithBlankValue() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        
        assertThrows(BadRequestException.class, () -> {
            userService.updateUserLocation(savedUser.getId(), "");
        });
    }

    @Test
    public void testDeleteUserById() {
        User user = userService.createUser("testuser", "password123", "test@example.com", "Moscow");
        User savedUser = userService.saveUser(user);
        userService.deleteUserById(savedUser.getId());
        assertThrows(RuntimeException.class, () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    public void testDeleteNonExistentUserById() {
        assertThatThrownBy(() -> userService.deleteUserById(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User with id 999 not found");
    }
}
