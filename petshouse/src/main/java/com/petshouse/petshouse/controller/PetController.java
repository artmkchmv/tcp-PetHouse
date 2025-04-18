package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.petshouse.petshouse.dto.pet.PetDto;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.enums.PetType;
import com.petshouse.petshouse.service.*;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @GetMapping("/available")
    public ResponseEntity<List<PetDto>> getAllAvailablePets() {
        List<PetDto> pets = petService.getAllAvailablePets().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<PetDto>> searchByName(@RequestParam String name) {
        List<PetDto> pets = petService.getPetsByName(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/search/type")
    public ResponseEntity<List<PetDto>> searchByType(@RequestParam PetType type) {
        List<PetDto> pets = petService.getPetsByType(type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(
            @RequestBody PetDto petDto,
            @RequestParam Long ownerId) {
        User owner = userService.getUserById(ownerId);
        Pet pet = petService.createPet(
                petDto.getPetName(),
                petDto.getPetAge(),
                petDto.getPetType(),
                petDto.getPetDescription(),
                petDto.getPetStatus(),
                owner,
                petDto.getPetPhotoURL()
        );
        petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(pet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        Pet updatedPet = petService.updatePet(id, petDto);
        return ResponseEntity.ok(toDto(updatedPet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    private PetDto toDto(Pet pet) {
        PetDto dto = new PetDto();
        dto.setPetName(pet.getPetName());
        dto.setPetAge(pet.getPetAge());
        dto.setPetType(pet.getPetType());
        dto.setPetDescription(pet.getPetDescription());
        dto.setPetStatus(pet.getPetStatus());
        dto.setPetPhotoURL(pet.getPetPhotoURL());
        return dto;
    }
}