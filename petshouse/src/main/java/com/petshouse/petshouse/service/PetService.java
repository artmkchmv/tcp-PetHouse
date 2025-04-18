package com.petshouse.petshouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.petshouse.petshouse.dto.pet.PetDto;
import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.enums.*;
import com.petshouse.petshouse.repository.PetRepository;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
        .orElseThrow(() -> new RuntimeException("Pet with id " + petId + " not found"));
    }
    
    public Pet createPet(
            String petName,
            int petAge,
            PetType petType,
            String petDescription,
            PetStatus petStatus,
            User petOwner,
            String petPhotoURL
            ) {
        Pet pet = new Pet();
        pet.setPetName(petName);
        pet.setPetAge(petAge);
        pet.setPetType(petType);
        pet.setPetDescription(petDescription);
        pet.setPetStatus(petStatus);
        pet.setPetOwner(petOwner);
        pet.setPetPhotoURL(petPhotoURL);
        return pet;
    }

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public List<Pet> getAllAvailablePets() {
        return petRepository.findByPetStatus(PetStatus.AVAILABLE);
    }

    public List<Pet> getPetsByName(String petName) {
        return petRepository.findByPetNameContainingIgnoreCase(petName);
    }

    public List<Pet> getPetsByType(PetType petType) {
        return petRepository.findByPetType(petType);
    }

    @Transactional
    public Pet updatePet(Long petId, PetDto request) {
        Pet pet = getPetById(petId);

        if (request.getPetName() != null && !request.getPetName().isBlank()) {
            pet.setPetName(request.getPetName());
        }

        if (request.getPetAge() != null && request.getPetAge() > 0) {
            pet.setPetAge(request.getPetAge());
        }

        if (request.getPetType() != null) { 
            pet.setPetType(request.getPetType());
        }

        if (request.getPetDescription() != null && !request.getPetDescription().isBlank()) {
            pet.setPetDescription(request.getPetDescription());
        }

        if (request.getPetStatus() != null) {
            pet.setPetStatus(request.getPetStatus());
        }

        if (request.getPetPhotoURL() != null && !request.getPetPhotoURL().isBlank()) {
            pet.setPetPhotoURL(request.getPetPhotoURL());
        }

        return savePet(pet);
    }

    @Transactional
    public void deletePet(Long petId) {
        Pet pet = getPetById(petId);
        petRepository.delete(pet);
    }
}
