package com.petshouse.petshouse.service;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.petshouse.petshouse.dto.jwt.*;
import com.petshouse.petshouse.dto.user.UserDto;
import com.petshouse.petshouse.dto.user.UserRegistrationRequest;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.mapper.UserMapper;
import com.petshouse.petshouse.security.JwtAuthentication;
import com.petshouse.petshouse.security.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtService;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public UserDto register(UserRegistrationRequest request) {
        User user = userService.createUser(
            request.getLogin(),
            request.getPassword(),
            request.getEmail(),
            request.getLocation()
        );
        return UserMapper.toDto(userService.saveUser(user));
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        if (userService.authenticateUser(authRequest.getLogin(), authRequest.getPassword())) {
            final User user = userService.getUserByLogin(authRequest.getLogin());
            final String accessToken = jwtService.generateAccessToken(user);
            final String refreshToken = jwtService.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new RuntimeException("Invalid login or password!");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtService.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login);
                final String accessToken = jwtService.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtService.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtService.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getUserByLogin(login);
                final String accessToken = jwtService.generateAccessToken(user);
                final String newRefreshToken = jwtService.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new RuntimeException("Invalid JWT!");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
