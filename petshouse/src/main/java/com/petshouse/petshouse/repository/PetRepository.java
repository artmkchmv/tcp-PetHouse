package com.petshouse.petshouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.enums.PetStatus;
import com.petshouse.petshouse.enums.PetType;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByPetStatus(PetStatus petStatus);
    List<Pet> findByPetNameContainingIgnoreCase(String petName);
    List<Pet> findByPetType(PetType petType);
}
