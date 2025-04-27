package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.pet.PetDto;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.enums.PetType;
import com.petshouse.petshouse.service.PetService;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    private final UserService userService;

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

    @PostMapping("/create")
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

    @PutMapping("/update/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        Pet updatedPet = petService.updatePet(id, petDto);
        return ResponseEntity.ok(toDto(updatedPet));
    }

    @DeleteMapping("/delete/{id}")
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
