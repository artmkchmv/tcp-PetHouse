package com.petshouse.petshouse;

import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setHashPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        User savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getLogin()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getLocation()).isEqualTo("Moscow");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches("password123", savedUser.getHashPassword()));
    }

    @Test
    public void testFindByLogin() {
        User user = new User();
        user.setLogin("testuser");
        user.setHashPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        userRepository.save(user);

        User foundUser = userRepository.findByLogin("testuser");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getLogin()).isEqualTo("testuser");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }
}
