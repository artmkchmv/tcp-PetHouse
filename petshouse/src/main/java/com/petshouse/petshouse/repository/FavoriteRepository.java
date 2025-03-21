package com.petshouse.petshouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.*;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
}
