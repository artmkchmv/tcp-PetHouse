package com.petshouse.petshouse.service;

import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        user.setHashPassword(user.getPassword());
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean authenticateUser(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(password, user.getPassword());
        }
        return false;
    }
}