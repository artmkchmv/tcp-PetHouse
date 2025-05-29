package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.pet.PetDto;
import com.petshouse.petshouse.dto.pet.PetIdRequest;
import com.petshouse.petshouse.dto.pet.PetNameRequest;
import com.petshouse.petshouse.dto.pet.PetTypeRequest;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.mapper.PetMapper;
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
                .map(PetMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pets);
    }

    @PostMapping("/id")
    public ResponseEntity<PetDto> getPetById(@RequestBody PetIdRequest request) {
        if (request.getPetId() == null) {
            throw new BadRequestException("Pet name must be provided");
        }
        Pet pet = petService.getPetById(request.getPetId());
        return ResponseEntity.ok(PetMapper.toDto(pet));
    }

    @PostMapping("/name")
    public ResponseEntity<List<PetDto>> getPetByName(@RequestBody PetNameRequest request) {
        if (request.getPetName() == null || request.getPetName().isBlank()) {
            throw new BadRequestException("Pet name must be provided");
        }

        List<PetDto> pets = petService.getPetsByName(request.getPetName()).stream()
                .map(PetMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/type")
    public ResponseEntity<List<PetDto>> getPetByType(@RequestBody PetTypeRequest request) {
        if (request.getPetType() == null) {
            throw new BadRequestException("Pet type must be provided");
        }

        List<PetDto> pets = petService.getPetsByType(request.getPetType()).stream()
                .map(PetMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pets);
    }

    @PostMapping("/create")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto request) {

        if (request.getPetOwnerId() == null) {
            throw new BadRequestException("Owner ID must be provided");
        }

        if (request.getPetName() == null || request.getPetName().isBlank()) {
            throw new BadRequestException("Pet name must be provided");
        }

        User owner = userService.getUserById(request.getPetOwnerId());

        Pet pet = petService.createPet(
                request.getPetName(),
                request.getPetAge(),
                request.getPetType(),
                request.getPetDescription(),
                request.getPetStatus(),
                owner,
                request.getPetPhotoURL()
        );

        Pet saved = petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(PetMapper.toDto(saved));
    }

    @PutMapping("/update")
    public ResponseEntity<PetDto> updatePet(@RequestBody PetDto request) {
        if (request.getPetId() == null) {
            throw new BadRequestException("Pet ID must be provided for update");
        }

        Pet updatedPet = petService.updatePet(request);
        return ResponseEntity.ok(PetMapper.toDto(updatedPet));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePet(@RequestBody PetIdRequest request) {
        if (request.getPetId() == null) {
            throw new BadRequestException("Pet ID must be provided for deletion");
        }

        petService.deletePet(request.getPetId());
        return ResponseEntity.noContent().build();
    }
}
