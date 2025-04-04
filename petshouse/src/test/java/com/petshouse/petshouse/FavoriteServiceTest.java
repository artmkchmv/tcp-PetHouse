package com.petshouse.petshouse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.petshouse.petshouse.entity.Favorite;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.FavoriteRepository;
import com.petshouse.petshouse.repository.PetRepository;
import com.petshouse.petshouse.repository.UserRepository;
import com.petshouse.petshouse.service.FavoriteService;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Test
    public void testAddFavorite() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword("password123");
        user.setEmail("user1@example.com");
        user.setLocation("Moscow");

        User savedUser = userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Bella");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser);
        pet.setPetPhotoURL("www.example.com/photo");

        Pet savedPet = petRepository.save(pet);

        Favorite favorite = new Favorite();
        favorite.setUser(savedUser);
        favorite.setPet(savedPet);

        Favorite savedFavorite = favoriteService.addFavorite(favorite);

        assertThat(savedFavorite).isNotNull();
        assertThat(savedFavorite.getId()).isNotNull();
        assertThat(savedFavorite.getUser()).isEqualTo(savedUser);
        assertThat(savedFavorite.getPet()).isEqualTo(savedPet);
    }

    @Test
    public void testRemoveFavorite() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword("password123");
        user.setEmail("user1@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Bella");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser);
        pet.setPetPhotoURL("www.example.com/photo");
        Pet savedPet = petRepository.save(pet);

        Favorite favorite = new Favorite();
        favorite.setUser(savedUser);
        favorite.setPet(savedPet);

        Favorite savedFavorite = favoriteRepository.save(favorite);

        favoriteService.removeFavorite(savedFavorite.getId());

        Optional<Favorite> deletedFavorite = favoriteRepository.findById(savedFavorite.getId());
        assertThat(deletedFavorite).isEmpty();
    }

    @Test
    public void testGetFavoritesByUser() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword("password123");
        user.setEmail("user1@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        Pet pet1 = new Pet();
        pet1.setPetName("Bella");
        pet1.setPetAge(3);
        pet1.setPetType("Dog");
        pet1.setPetDescription("Friendly dog");
        pet1.setPetStatus("available");
        pet1.setPetOwner(savedUser);
        pet1.setPetPhotoURL("www.example.com/photo1");
        Pet savedPet1 = petRepository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setPetName("Lucy");
        pet2.setPetAge(2);
        pet2.setPetType("Cat");
        pet2.setPetDescription("Playful cat");
        pet2.setPetStatus("available");
        pet2.setPetOwner(savedUser);
        pet2.setPetPhotoURL("www.example.com/photo2");
        Pet savedPet2 = petRepository.save(pet2);

        Favorite favorite1 = new Favorite();
        favorite1.setUser(savedUser);
        favorite1.setPet(savedPet1);

        favoriteRepository.save(favorite1);

        Favorite favorite2 = new Favorite();
        favorite2.setUser(savedUser);
        favorite2.setPet(savedPet2);

        favoriteRepository.save(favorite2);

        List<Favorite> favorites = favoriteService.getFavoritesByUser(savedUser.getId());

        assertThat(favorites).hasSize(2);
        assertThat(favorites).extracting(Favorite::getUser).contains(savedUser, savedUser);
        assertThat(favorites).extracting(Favorite::getPet).contains(savedPet1, savedPet2);
    }

    @Test
    public void testGetFavoritesByUser_NoFavorites() {
        User user = new User();
        user.setLogin("user2");
        user.setPassword("password123");
        user.setEmail("user2@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        List<Favorite> favorites = favoriteService.getFavoritesByUser(savedUser.getId());

        assertThat(favorites).isEmpty();
    }
}
