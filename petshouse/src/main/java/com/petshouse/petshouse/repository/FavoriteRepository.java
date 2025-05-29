package com.petshouse.petshouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.*;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);
    
    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.pet.id = :petId")
    Optional<Favorite> findByUserIdAndPetId(@Param("userId") Long userId, @Param("petId") Long petId);
}
