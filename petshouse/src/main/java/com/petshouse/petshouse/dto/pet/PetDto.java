package com.petshouse.petshouse.dto.pet;

import lombok.Getter;
import lombok.Setter;

import com.petshouse.petshouse.enums.PetStatus;
import com.petshouse.petshouse.enums.PetType;

@Getter
@Setter
public class PetDto {
    private String petName;
    private Integer petAge;
    private PetType petType;
    private String petDescription;
    private PetStatus petStatus;
    private String petPhotoURL;
}
