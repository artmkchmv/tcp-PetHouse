package com.petshouse.petshouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.petshouse.petshouse.dto.user.*;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(userService.getUserById(id)));
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDto> getUserByLogin(@PathVariable String login) {
        return ResponseEntity.ok(toDto(userService.getUserByLogin(login)));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegistrationRequest request) {
        User user = userService.createUser(
            request.getLogin(),
            request.getPassword(),
            request.getEmail(),
            request.getLocation()
        );
        return new ResponseEntity<>(toDto(userService.saveUser(user)), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginRequest request) {
        boolean authenticated = userService.authenticateUser(request.getLogin(), request.getPassword());
        return authenticated
            ? ResponseEntity.ok("Authentication successful")
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login or password");
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<UserDto> updateEmail(@PathVariable Long id, @RequestBody UserEmailUpdateRequest request) {
        return ResponseEntity.ok(toDto(userService.updateUserEmail(id, request.getNewEmail())));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateRequest request) {
        return ResponseEntity.ok(toDto(userService.updateUserPassword(id, request.getNewPassword())));
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<UserDto> updateLocation(@PathVariable Long id, @RequestBody UserLocationUpdateRequest request) {
        return ResponseEntity.ok(toDto(userService.updateUserLocation(id, request.getNewLocation())));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getLogin(),
            user.getEmail(),
            user.getLocation(),
            user.getRegistrationDate()
        );
    }
}
