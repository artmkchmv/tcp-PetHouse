package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.favorite.FavoriteAddRequest;
import com.petshouse.petshouse.dto.favorite.FavoriteDto;
import com.petshouse.petshouse.dto.favorite.FavoriteIdRequest;
import com.petshouse.petshouse.entity.Favorite;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.mapper.FavoriteMapper;
import com.petshouse.petshouse.security.JwtAuthentication;
import com.petshouse.petshouse.service.AuthService;
import com.petshouse.petshouse.service.FavoriteService;
import com.petshouse.petshouse.service.PetService;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final UserService userService;

    private final PetService petService;

    private final AuthService authService;

    @PostMapping("/add")
    public ResponseEntity<FavoriteDto> addFavorite(@RequestBody FavoriteAddRequest request) {
        User user = userService.getUserById(request.getUserId());
        Pet pet = petService.getPetById(request.getPetId());

        Favorite favorite = favoriteService.createFavorite(user, pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(FavoriteMapper.toDto(favorite));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteIdRequest request) {
        favoriteService.deleteFavorite(request.getFavId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleted")
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteAddRequest request) {
        favoriteService.deleteByUserIdAndPetId(request.getUserId(), request.getPetId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Pet>> getFavoritePetsByUser() {
        JwtAuthentication auth = authService.getAuthInfo();
        String login = auth.getLogin();
        User user = userService.getUserByLogin(login);

        List<Favorite> favorites = favoriteService.getFavoritesByUserId(user.getId());

        List<Pet> favoritePets = favorites.stream()
            .map(Favorite::getPet)
            .collect(Collectors.toList());

        return ResponseEntity.ok(favoritePets);
    }
}
