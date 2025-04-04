package com.petshouse.petshouse;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.repository.*;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class FavoriteRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    public void testCreateFavorite() {
        User user = new User();
        user.setLogin("testuser1");
        user.setHashPassword("password1231");
        user.setEmail("test1@example.com");
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

        Favorite favorite = new Favorite();
        favorite.setUser(savedUser);
        favorite.setPet(savedPet);

        Favorite savedFavorite = favoriteRepository.save(favorite);

        assertThat(savedFavorite).isNotNull();
        assertThat(savedFavorite.getId()).isNotNull();
        assertThat(savedFavorite.getUser()).isEqualTo(savedUser);
        assertThat(savedFavorite.getPet()).isEqualTo(savedPet);
    }

    @Test
    public void testFindByUserId() {
        User user = new User();
        user.setLogin("testuser");
        user.setHashPassword("password123");
        user.setEmail("test@example.com");
        user.setLocation("Moscow");

        userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(user);
        pet.setPetPhotoURL("example.com");

        petRepository.save(pet);

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPet(pet);

        favoriteRepository.save(favorite);

        List<Favorite> favorites = favoriteRepository.findByUser_Id(user.getId());

        assertThat(favorites).isNotEmpty();
        assertThat(favorites.get(0).getUser()).isEqualTo(user);
        assertThat(favorites.get(0).getPet()).isEqualTo(pet);
    }
}
