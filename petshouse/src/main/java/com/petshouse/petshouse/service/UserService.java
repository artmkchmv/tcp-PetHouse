package com.petshouse.petshouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
    }
    
    public User getUserByLogin(String userLogin) {
        return userRepository.findByLogin(userLogin)
        .orElseThrow(() -> new RuntimeException("User with login " + userLogin + " not found"));
    }

    public User createUser(
            String userLogin, 
            String userPassword, 
            String userEmail, 
            String userLocation
            ) {
        User user = new User();
        user.setLogin(userLogin);
        user.setPassword(hashPassword(userPassword));
        user.setEmail(userEmail);
        user.setLocation(userLocation);
        return user;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean authenticateUser(String userLogin, String userPassword) {
        User user = getUserByLogin(userLogin);
        return passwordEncoder.matches(userPassword, user.getPassword());
    }

    @Transactional
    public User updateUserPassword(Long userId, String newUserPassword) {
        User user = getUserById(userId);
        if (newUserPassword != null && !newUserPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newUserPassword));
        }
        return saveUser(user);
    }

    @Transactional
    public User updateUserEmail(Long userId, String newUserEmail) {
        User user = getUserById(userId);
        if (newUserEmail != null && !newUserEmail.isBlank()) {
            user.setEmail(newUserEmail);
        }
        return saveUser(user);
    }

    @Transactional
    public User updateUserLocation(Long userId, String newUserLocation) {
        User user = getUserById(userId);
        if (newUserLocation != null && !newUserLocation.isBlank()) {
            user.setLocation(newUserLocation);
        }
        return saveUser(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}
