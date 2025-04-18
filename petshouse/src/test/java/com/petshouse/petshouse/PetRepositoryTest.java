package com.petshouse.petshouse;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.enums.*;
import com.petshouse.petshouse.repository.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PetRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    public void testFindByPetStatus() {
        User testUser = new User();
        testUser.setLogin("testUser");
        testUser.setPassword(passwordEncoder.encode("testpassword"));
        testUser.setEmail("testuser@example.com");
        testUser.setLocation("Test Location");
        userRepository.save(testUser);

        Pet pet1 = new Pet();
        pet1.setPetName("Friendly Dog");
        pet1.setPetAge(2);
        pet1.setPetType(PetType.DOG);
        pet1.setPetDescription("Friendly dog");
        pet1.setPetStatus(PetStatus.AVAILABLE);
        pet1.setPetOwner(testUser);
        pet1.setPetPhotoURL("url1");

        Pet pet2 = new Pet();
        pet2.setPetName("Cat friendly");
        pet2.setPetAge(3);
        pet2.setPetType(PetType.CAT);
        pet2.setPetDescription("Cute cat");
        pet2.setPetStatus(PetStatus.UNAVAILABLE);
        pet2.setPetOwner(testUser);
        pet2.setPetPhotoURL("url2");

        petRepository.save(pet1);
        petRepository.save(pet2);

        List<Pet> availablePets = petRepository.findByPetStatus(PetStatus.AVAILABLE);
        List<Pet> unavailablePets = petRepository.findByPetStatus(PetStatus.UNAVAILABLE);

        assertThat(availablePets).hasSize(1);
        assertThat(availablePets.get(0).getPetName()).isEqualTo("Friendly Dog");
        assertThat(unavailablePets).hasSize(1);
        assertThat(unavailablePets.get(0).getPetName()).isEqualTo("Cat friendly");
    }

    @Test
    public void testFindByPetNameContainingIgnoreCase() {
        User testUser = new User();
        testUser.setLogin("testUser");
        testUser.setPassword(passwordEncoder.encode("testpassword"));
        testUser.setEmail("testuser@example.com");
        testUser.setLocation("Test Location");
        userRepository.save(testUser);

        Pet pet1 = new Pet();
        pet1.setPetName("Friendly Dog");
        pet1.setPetAge(2);
        pet1.setPetType(PetType.DOG);
        pet1.setPetDescription("Friendly dog");
        pet1.setPetStatus(PetStatus.AVAILABLE);
        pet1.setPetOwner(testUser);
        pet1.setPetPhotoURL("url1");

        Pet pet2 = new Pet();
        pet2.setPetName("Cat friendly");
        pet2.setPetAge(3);
        pet2.setPetType(PetType.CAT);
        pet2.setPetDescription("Cute cat");
        pet2.setPetStatus(PetStatus.UNAVAILABLE);
        pet2.setPetOwner(testUser);
        pet2.setPetPhotoURL("url2");

        petRepository.save(pet1);
        petRepository.save(pet2);

        List<Pet> petsWithNameFriendly = petRepository.findByPetNameContainingIgnoreCase("friendly");

        assertThat(petsWithNameFriendly).hasSize(2);
        assertThat(petsWithNameFriendly.get(0).getPetName()).contains("Friendly");
        assertThat(petsWithNameFriendly.get(1).getPetName()).contains("friendly");
    }

    @Test
    public void testFindByPetType() {
        User testUser = new User();
        testUser.setLogin("testUser");
        testUser.setPassword(passwordEncoder.encode("testpassword"));
        testUser.setEmail("testuser@example.com");
        testUser.setLocation("Test Location");
        userRepository.save(testUser);

        Pet pet1 = new Pet();
        pet1.setPetName("Dog1");
        pet1.setPetAge(2);
        pet1.setPetType(PetType.DOG);
        pet1.setPetDescription("Friendly dog");
        pet1.setPetStatus(PetStatus.AVAILABLE);
        pet1.setPetOwner(testUser);
        pet1.setPetPhotoURL("url1");

        Pet pet2 = new Pet();
        pet2.setPetName("Cat1");
        pet2.setPetAge(3);
        pet2.setPetType(PetType.CAT);
        pet2.setPetDescription("Cute cat");
        pet2.setPetStatus(PetStatus.UNAVAILABLE);
        pet2.setPetOwner(testUser);
        pet2.setPetPhotoURL("url2");

        petRepository.save(pet1);
        petRepository.save(pet2);

        List<Pet> dogs = petRepository.findByPetType(PetType.DOG);
        List<Pet> cats = petRepository.findByPetType(PetType.CAT);

        assertThat(dogs).hasSize(1);
        assertThat(dogs.get(0).getPetName()).isEqualTo("Dog1");
        assertThat(cats).hasSize(1);
        assertThat(cats.get(0).getPetName()).isEqualTo("Cat1");
    }
}