package com.petshouse.petshouse.dto.pet;

import com.petshouse.petshouse.enums.PetType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetTypeRequest {

    private PetType petType;
}
