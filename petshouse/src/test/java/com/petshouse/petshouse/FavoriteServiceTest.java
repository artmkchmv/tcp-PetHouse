package com.petshouse.petshouse;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.repository.*;
import com.petshouse.petshouse.enums.*;
import com.petshouse.petshouse.service.FavoriteService;

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

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Favorite createFavorite(User user, Pet pet) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPet(pet);
        return favorite;
    }

    @Test
    public void testCreateFavorite() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword(passwordEncoder.encode("password1"));
        user.setEmail("user1@example.com");
        user.setLocation("Location1");
        userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Friendly Dog");
        pet.setPetAge(2);
        pet.setPetType(PetType.DOG);
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus(PetStatus.AVAILABLE);
        pet.setPetOwner(user);
        pet.setPetPhotoURL("url1");
        petRepository.save(pet);

        Favorite favorite = favoriteService.createFavorite(user, pet);

        assertThat(favorite).isNotNull();
        assertThat(favorite.getUser()).isEqualTo(user);
        assertThat(favorite.getPet()).isEqualTo(pet);
    }

    @Test
    public void testDeleteFavorite() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword(passwordEncoder.encode("password1"));
        user.setEmail("user1@example.com");
        user.setLocation("Location1");
        userRepository.save(user);

        Pet pet = new Pet();
        pet.setPetName("Friendly Dog");
        pet.setPetAge(2);
        pet.setPetType(PetType.DOG);
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus(PetStatus.AVAILABLE);
        pet.setPetOwner(user);
        pet.setPetPhotoURL("url1");
        petRepository.save(pet);

        Favorite favorite = favoriteService.createFavorite(user, pet);

        favoriteService.deleteFavorite(favorite.getId());

        Optional<Favorite> deletedFavorite = favoriteRepository.findById(favorite.getId());
        assertThat(deletedFavorite).isEmpty();
    }

    @Test
    public void testGetFavoritesByUserId() {
        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword(passwordEncoder.encode("password1"));
        user1.setEmail("user1@example.com");
        user1.setLocation("Location1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setLogin("user2");
        user2.setPassword(passwordEncoder.encode("password2"));
        user2.setEmail("user2@example.com");
        user2.setLocation("Location2");
        userRepository.save(user2);

        Pet pet1 = new Pet();
        pet1.setPetName("Friendly Dog");
        pet1.setPetAge(2);
        pet1.setPetType(PetType.DOG);
        pet1.setPetDescription("Friendly dog");
        pet1.setPetStatus(PetStatus.AVAILABLE);
        pet1.setPetOwner(user1);
        pet1.setPetPhotoURL("url1");
        petRepository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setPetName("Cute Cat");
        pet2.setPetAge(1);
        pet2.setPetType(PetType.CAT);
        pet2.setPetDescription("Cute cat");
        pet2.setPetStatus(PetStatus.AVAILABLE);
        pet2.setPetOwner(user2);
        pet2.setPetPhotoURL("url2");
        petRepository.save(pet2);

        favoriteService.createFavorite(user1, pet1);
        favoriteService.createFavorite(user1, pet2);
        favoriteService.createFavorite(user2, pet1);

        List<Favorite> favoritesForUser1 = favoriteService.getFavoritesByUserId(user1.getId());

        assertThat(favoritesForUser1).hasSize(2);
        assertThat(favoritesForUser1.get(0).getPet()).isEqualTo(pet1);
        assertThat(favoritesForUser1.get(1).getPet()).isEqualTo(pet2);

        List<Favorite> favoritesForUser2 = favoriteService.getFavoritesByUserId(user2.getId());

        assertThat(favoritesForUser2).hasSize(1);
        assertThat(favoritesForUser2.get(0).getPet()).isEqualTo(pet1);
    }

    @Test
    public void testGetFavoritesByUserIdWhenNoFavorites() {
        User user = new User();
        user.setLogin("user3");
        user.setPassword(passwordEncoder.encode("password3"));
        user.setEmail("user3@example.com");
        user.setLocation("Location3");
        userRepository.save(user);

        List<Favorite> favorites = favoriteService.getFavoritesByUserId(user.getId());

        assertThat(favorites).isEmpty();
    }
}
