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
        user.setPassword("password1231");
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

        FavoriteId favoriteId = new FavoriteId();
        favoriteId.setUser(savedUser.getId());
        favoriteId.setPet(savedPet.getPetId());

        Favorite favorite = new Favorite();
        favorite.setId(favoriteId);
        favorite.setUser(savedUser);
        favorite.setPet(savedPet);

        Favorite savedFavorite = favoriteRepository.save(favorite);

        assertThat(savedFavorite).isNotNull();
        assertThat(savedFavorite.getId()).isNotNull();
        assertThat(savedFavorite.getUser()).isEqualTo(savedUser);
        assertThat(savedFavorite.getPet()).isEqualTo(pet);
    }
}
