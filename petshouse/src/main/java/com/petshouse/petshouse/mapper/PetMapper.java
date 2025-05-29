package com.petshouse.petshouse.mapper;

import com.petshouse.petshouse.dto.pet.PetDto;
import com.petshouse.petshouse.entity.Pet;

public class PetMapper {

    private PetMapper() {}

    public static PetDto toDto(Pet pet) {
        if (pet == null) {
            return null;
        }
        PetDto dto = new PetDto();
        dto.setPetName(pet.getPetName());
        dto.setPetAge(pet.getPetAge());
        dto.setPetType(pet.getPetType());
        dto.setPetDescription(pet.getPetDescription());
        dto.setPetStatus(pet.getPetStatus());
        dto.setPetOwnerId(pet.getPetOwner().getId());
        dto.setPetPhotoURL(pet.getPetPhotoURL());
        return dto;
    }
}
