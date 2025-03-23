package com.petshouse.petshouse;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PetRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Test
    public void testCreatePet() {
        User user = new User();
        user.setLogin("testuser");
        user.setHashPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        User savedUser = userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Ashley");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Good boy");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser);
        pet.setPetPhotoURL("www.example.com");

        Pet savedPet = petRepository.save(pet);

        assertThat(savedPet).isNotNull();
        assertThat(savedPet.getPetId()).isNotNull();
        assertThat(savedPet.getPetName()).isEqualTo("Ashley");
        assertThat(savedPet.getPetAge()).isEqualTo(3);
        assertThat(savedPet.getPetType()).isEqualTo("Dog");
        assertThat(savedPet.getPetDescription()).isEqualTo("Good boy");
        assertThat(savedPet.getPetStatus()).isEqualTo("available");
        assertThat(savedPet.getPetOwner()).isEqualTo(savedUser);
        assertThat(savedPet.getPetPhotoURL()).isEqualTo("www.example.com");
    }
}
