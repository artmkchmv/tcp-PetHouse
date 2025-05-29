package com.petshouse.petshouse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.pet.PetDto;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.enums.PetStatus;
import com.petshouse.petshouse.enums.PetType;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.exceptions.ResourceNotFoundException;
import com.petshouse.petshouse.repository.PetRepository;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
        .orElseThrow(() -> new ResourceNotFoundException("Pet with id " + petId + " not found"));
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
        if (petName == null || petName.isBlank()) {
            throw new BadRequestException("Pet name must not be empty");
        }
        if (petAge <= 0) {
            throw new BadRequestException("Pet age must be greater than 0");
        }
        if (petType == null) {
            throw new BadRequestException("Pet type must be specified");
        }
        if (petOwner == null) {
            throw new BadRequestException("Pet owner must be provided");
        }

        Pet pet = new Pet();
        pet.setPetName(petName);
        pet.setPetAge(petAge);
        pet.setPetType(petType);
        pet.setPetDescription(petDescription);
        pet.setPetStatus(petStatus);
        pet.setPetOwner(petOwner);

        if (petPhotoURL == null || petPhotoURL.isBlank()) {
            String defaultPhoto = getDefaultPhotoByType(petType);
            pet.setPetPhotoURL(defaultPhoto);
        } else {
            pet.setPetPhotoURL(petPhotoURL);
        }

        return pet;
    }

    private String getDefaultPhotoByType(PetType petType) {
        return switch (petType) {
            case DOG -> "/images/dog.png";
            case CAT -> "/images/cat.png";
            case FISH -> "/images/fish.png";
            case FROG -> "/images/frog.png";
            case HORSE -> "/images/horse.png";
            case SPIDER -> "/images/spider.png";
            case CROW -> "/images/crow.png";
            case DOVE -> "/images/dove.png";
            case DRAGON -> "/images/dragon.png";
        };
    }

    public List<Pet> getAllAvailablePets() {
        return petRepository.findByPetStatus(PetStatus.AVAILABLE);
    }

    public List<Pet> getPetsByName(String petName) {
        if (petName == null || petName.isBlank()) {
            throw new BadRequestException("Pet name must not be empty");
        }
        return petRepository.findByPetNameContainingIgnoreCase(petName);
    }

    public List<Pet> getPetsByType(PetType petType) {
        if (petType == null) {
            throw new BadRequestException("Pet type must be specified");
        }
        return petRepository.findByPetType(petType);
    }

    @Transactional
    public Pet updatePet(PetDto request) {
        Pet pet = getPetById(request.getPetId());

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
