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
public class MessageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void testCreateMessage() {
        User user1 = new User();
        user1.setLogin("testuser1");
        user1.setHashPassword("password1231");
        user1.setEmail("test1@example.com");
        user1.setLocation("Moscow");

        User user2 = new User();
        user2.setLogin("testuser2");
        user2.setHashPassword("password1232");
        user2.setEmail("test2@example.com");
        user2.setLocation("Washington");

        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        Pet pet = new Pet();
        pet.setPetName("Ashley");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Good boy");
        pet.setPetStatus("available");
        pet.setPetOwner(savedUser1);
        pet.setPetPhotoURL("www.example.com");

        Pet savedPet = petRepository.save(pet);

        Message message = new Message();
        message.setSender(savedUser2);
        message.setReceiver(savedUser1);
        message.setPet(savedPet);
        message.setMessageText("Hello");

        Message savedMessage = messageRepository.save(message);

        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.getMessageId()).isNotNull();
        assertThat(savedMessage.getSender()).isEqualTo(savedUser2);
        assertThat(savedMessage.getReceiver()).isEqualTo(savedUser1);
        assertThat(savedMessage.getPet()).isEqualTo(savedPet);
        assertThat(savedMessage.getMessageText()).isEqualTo("Hello");
    }
}
