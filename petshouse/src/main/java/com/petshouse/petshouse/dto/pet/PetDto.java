package com.petshouse.petshouse.dto.pet;

import lombok.*;

import com.petshouse.petshouse.enums.*;

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
