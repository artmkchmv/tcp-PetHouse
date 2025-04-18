package com.petshouse.petshouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.repository.FavoriteRepository;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public Favorite createFavorite(User user, Pet pet) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPet(pet);
        return saveFavorite(favorite);
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
