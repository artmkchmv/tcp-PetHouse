package com.petshouse.petshouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.user.UserDto;
import com.petshouse.petshouse.dto.user.UserEmailUpdateRequest;
import com.petshouse.petshouse.dto.user.UserLocationUpdateRequest;
import com.petshouse.petshouse.dto.user.UserPasswordUpdateRequest;
import com.petshouse.petshouse.mapper.UserMapper;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.toDto(userService.getUserById(id)));
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDto> getUserByLogin(@PathVariable String login) {
        return ResponseEntity.ok(UserMapper.toDto(userService.getUserByLogin(login)));
    }

    @PatchMapping("/update/{id}/email")
    public ResponseEntity<UserDto> updateEmail(@PathVariable Long id, @RequestBody UserEmailUpdateRequest request) {
        return ResponseEntity.ok(UserMapper.toDto(userService.updateUserEmail(id, request.getNewEmail())));
    }

    @PatchMapping("/update/{id}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateRequest request) {
        return ResponseEntity.ok(UserMapper.toDto(userService.updateUserPassword(id, request.getNewPassword())));
    }

    @PatchMapping("/update/{id}/location")
    public ResponseEntity<UserDto> updateLocation(@PathVariable Long id, @RequestBody UserLocationUpdateRequest request) {
        return ResponseEntity.ok(UserMapper.toDto(userService.updateUserLocation(id, request.getNewLocation())));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
