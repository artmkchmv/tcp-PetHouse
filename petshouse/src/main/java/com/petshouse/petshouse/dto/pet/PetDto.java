package com.petshouse.petshouse.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.petshouse.petshouse.enums.PetStatus;
import com.petshouse.petshouse.enums.PetType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {
    
    private Long petId;
    private String petName;
    private Integer petAge;
    private PetType petType;
    private String petDescription;
    private PetStatus petStatus;
    private Long petOwnerId;
    private String petPhotoURL;
}
