package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.favorite.FavoriteDto;
import com.petshouse.petshouse.entity.Favorite;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.service.FavoriteService;
import com.petshouse.petshouse.service.PetService;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final UserService userService;

    private final PetService petService;

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
