package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.petshouse.petshouse.dto.favorite.FavoriteDto;
import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.service.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<FavoriteDto> addFavorite(@RequestParam Long userId, @RequestParam Long petId) {
        User user = userService.getUserById(userId);
        Pet pet = petService.getPetById(petId);

        Favorite favorite = favoriteService.createFavorite(user, pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(favorite));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteDto>> getFavoritesByUser(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        List<FavoriteDto> favoriteDtos = favorites.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(favoriteDtos);
    }

    private FavoriteDto toDto(Favorite favorite) {
        return new FavoriteDto(favorite.getId(), favorite.getUser().getId(), favorite.getPet().getPetId());
    }
}