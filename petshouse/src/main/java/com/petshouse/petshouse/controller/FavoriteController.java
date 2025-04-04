package com.petshouse.petshouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.service.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;
    
    @PostMapping("/{petId}")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long petId, @RequestParam Long userId) {
        Pet pet = petService.getPetById(petId);
        User user = userService.getUserById(userId);

        Favorite favorite = new Favorite();
        favorite.setPet(pet);
        favorite.setUser(user);

        Favorite savedFavorite = favoriteService.addFavorite(favorite);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFavorite);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long petId, @RequestParam Long userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUser(userId);
        for (Favorite favorite : favorites) {
            if (favorite.getPet().getPetId().equals(petId)) {
                favoriteService.removeFavorite(favorite.getId());
                break;
            }
        }
        return ResponseEntity.noContent().build();
    }
}