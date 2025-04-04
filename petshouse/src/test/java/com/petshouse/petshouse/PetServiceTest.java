package com.petshouse.petshouse;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.PetRepository;
import com.petshouse.petshouse.repository.UserRepository;
import com.petshouse.petshouse.service.PetService;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PetServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddPet() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser);
        pet.setPetPhotoURL("www.example.com/photo");

        Pet savedPet = petService.addPet(pet);

        assertThat(savedPet).isNotNull();
        assertThat(savedPet.getPetId()).isNotNull();
        assertThat(savedPet.getPetName()).isEqualTo("Buddy");
        assertThat(savedPet.getPetAge()).isEqualTo(3);
        assertThat(savedPet.getPetType()).isEqualTo("Dog");
        assertThat(savedPet.getPetDescription()).isEqualTo("Friendly dog");
        assertThat(savedPet.getPetOwner()).isEqualTo(savedUser);
    }

    @Test
    public void testGetPetById() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        User savedUser = userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser);
        pet.setPetPhotoURL("www.example.com/photo");

        Pet savedPet = petRepository.save(pet);

        Pet foundPet = petService.getPetById(savedPet.getPetId());

        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getPetId()).isEqualTo(savedPet.getPetId());
    }

    @Test
    public void testDeletePet() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser);
        pet.setPetPhotoURL("www.example.com/photo");

        Pet savedPet = petRepository.save(pet);

        petService.deletePet(savedPet.getPetId());

        Optional<Pet> deletedPet = petRepository.findById(savedPet.getPetId());
        assertThat(deletedPet).isEmpty();
    }

    @Test
    public void testGetAllPets() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        Pet pet1 = new Pet();
        pet1.setPetName("Buddy");
        pet1.setPetAge(3);
        pet1.setPetType("Dog");
        pet1.setPetDescription("Friendly dog");
        pet1.setPetStatus("available");
        pet1.setPetOwner(savedUser);
        pet1.setPetPhotoURL("www.example.com/photo1");

        Pet pet2 = new Pet();
        pet2.setPetName("Lucy");
        pet2.setPetAge(2);
        pet2.setPetType("Cat");
        pet2.setPetDescription("Playful cat");
        pet2.setPetStatus("available");
        pet2.setPetOwner(savedUser);
        pet2.setPetPhotoURL("www.example.com/photo2");

        petRepository.save(pet1);
        petRepository.save(pet2);

        List<Pet> allPets = petService.getAllPets();

        assertThat(allPets).hasSize(2);
        assertThat(allPets).usingRecursiveComparison()
        .ignoringFields("petOwner.registrationDate")
        .isEqualTo(List.of(pet1, pet2));
    }

    @Test
    public void testGetPetById_PetNotFound() {
        Long nonExistentPetId = 999L;

        assertThrows(RuntimeException.class, () -> petService.getPetById(nonExistentPetId));
    }
}
