package com.petshouse.petshouse.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.entity.Favorite;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.exceptions.ResourceNotFoundException;
import com.petshouse.petshouse.repository.FavoriteRepository;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
    
    public Favorite createFavorite(User user, Pet pet) {
        if (user == null || pet == null) {
            throw new BadRequestException("User and pet must not be null");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPet(pet);
        return saveFavorite(favorite);
    }

    public void deleteFavorite(Long favoriteId) {
        if (!favoriteRepository.existsById(favoriteId)) {
            throw new ResourceNotFoundException("Favorite with id " + favoriteId + " not found");
        }
        favoriteRepository.deleteById(favoriteId);
    }

    public void deleteByUserIdAndPetId(Long userId, Long petId) {
        Favorite favorite = favoriteRepository.findByUserIdAndPetId(userId, petId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Favorite not found for userId " + userId + " and petId " + petId));
        favoriteRepository.delete(favorite);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        if (userId == null) {
            throw new BadRequestException("User ID must not be null");
        }
        return favoriteRepository.findByUserId(userId);
    }

    public List<Pet> getFavoritePetsByUser(User user) {
        if (user == null || user.getId() == null) {
            throw new BadRequestException("User or user ID must not be null");
        }
        return favoriteRepository.findByUserId(user.getId())
                                .stream()
                                .map(Favorite::getPet)
                                .collect(Collectors.toList());
    }
}
