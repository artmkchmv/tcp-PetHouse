package com.petshouse.petshouse.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.exceptions.ResourceNotFoundException;
import com.petshouse.petshouse.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        return userRepository.save(user);
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

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }
    
    public User getUserByLogin(String userLogin) {
        return userRepository.findByLogin(userLogin)
        .orElseThrow(() -> new ResourceNotFoundException("User with login " + userLogin + " not found"));
    }

    public boolean authenticateUser(String userLogin, String userPassword) {
        User user = getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(userPassword, user.getPassword());
    }

    @Transactional
    public User updateUserPassword(Long userId, String newUserPassword) {
        if (newUserPassword == null || newUserPassword.isBlank()) {
            throw new BadRequestException("New password must not be empty");
        }
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newUserPassword));
        return saveUser(user);
    }

    @Transactional
    public User updateUserEmail(Long userId, String newUserEmail) {
        if (newUserEmail == null || newUserEmail.isBlank()) {
            throw new BadRequestException("New email must not be empty");
        }
        User user = getUserById(userId);
        user.setEmail(newUserEmail);
        return saveUser(user);
    }

    @Transactional
    public User updateUserLocation(Long userId, String newUserLocation) {
        if (newUserLocation == null || newUserLocation.isBlank()) {
            throw new BadRequestException("New location must not be empty");
        }
        User user = getUserById(userId);
        user.setLocation(newUserLocation);
        return saveUser(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
