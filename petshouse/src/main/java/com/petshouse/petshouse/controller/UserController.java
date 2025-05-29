package com.petshouse.petshouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.user.IdRequest;
import com.petshouse.petshouse.dto.user.LoginRequest;
import com.petshouse.petshouse.dto.user.UserDto;
import com.petshouse.petshouse.dto.user.UserEmailUpdateRequest;
import com.petshouse.petshouse.dto.user.UserLocationUpdateRequest;
import com.petshouse.petshouse.dto.user.UserPasswordUpdateRequest;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.exceptions.UnauthorizedException;
import com.petshouse.petshouse.mapper.UserMapper;
import com.petshouse.petshouse.security.JwtAuthentication;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        JwtAuthentication auth = (JwtAuthentication) authentication;
        if (auth == null || auth.getLogin() == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        User user = userService.getUserByLogin(auth.getLogin());
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @GetMapping("/id")
    public ResponseEntity<UserDto> getUserById(@RequestBody IdRequest request) {
        if (request.getId() == null) {
            throw new BadRequestException("User ID must be provided");
        }

        User user = userService.getUserById(request.getId());
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> getUserByLogin(@RequestBody LoginRequest request) {
        if (request.getLogin() == null || request.getLogin().isBlank()) {
            throw new BadRequestException("Login must be provided");
        }

        User user = userService.getUserByLogin(request.getLogin());
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PatchMapping("/update/email")
    public ResponseEntity<UserDto> updateEmail(@RequestBody UserEmailUpdateRequest request) {
        if (request.getId() == null || request.getNewEmail() == null || request.getNewEmail().isBlank()) {
            throw new BadRequestException("User ID and new email must be provided");
        }

        User updatedUser = userService.updateUserEmail(request.getId(), request.getNewEmail());
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @PatchMapping("/update/password")
    public ResponseEntity<UserDto> updatePassword(@RequestBody UserPasswordUpdateRequest request) {
        if (request.getId() == null || request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new BadRequestException("User ID and new password must be provided");
        }

        User updatedUser = userService.updateUserPassword(request.getId(), request.getNewPassword());
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @PatchMapping("/update/location")
    public ResponseEntity<UserDto> updateLocation(@RequestBody UserLocationUpdateRequest request) {
        if (request.getId() == null || request.getNewLocation() == null || request.getNewLocation().isBlank()) {
            throw new BadRequestException("User ID and new location must be provided");
        }

        User updatedUser = userService.updateUserLocation(request.getId(), request.getNewLocation());
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody IdRequest request) {
        if (request.getId() == null) {
            throw new BadRequestException("User ID must be provided");
        }

        userService.deleteUserById(request.getId());
        return ResponseEntity.noContent().build();
    }
}
