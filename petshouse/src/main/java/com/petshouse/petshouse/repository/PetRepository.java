package com.petshouse.petshouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
