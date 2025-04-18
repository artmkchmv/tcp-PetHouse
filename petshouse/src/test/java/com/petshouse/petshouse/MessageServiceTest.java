package com.petshouse.petshouse;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.enums.*;
import com.petshouse.petshouse.repository.*;
import com.petshouse.petshouse.service.MessageService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSaveMessage() {
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

        Pet pet = new Pet();
        pet.setPetName("Friendly Dog");
        pet.setPetAge(2);
        pet.setPetType(PetType.DOG);
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus(PetStatus.AVAILABLE);
        pet.setPetOwner(user1);
        pet.setPetPhotoURL("url1");
        petRepository.save(pet);

        Message message = messageService.createMessage(user1, user2, pet, "Hello, I am interested in the dog.");
        
        Message savedMessage = messageService.saveMessage(message);

        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.getSender()).isEqualTo(user1);
        assertThat(savedMessage.getReceiver()).isEqualTo(user2);
        assertThat(savedMessage.getMessageText()).isEqualTo("Hello, I am interested in the dog.");
    }

    @Test
    public void testGetConversation() {
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

        Pet pet = new Pet();
        pet.setPetName("Friendly Dog");
        pet.setPetAge(2);
        pet.setPetType(PetType.DOG);
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus(PetStatus.AVAILABLE);
        pet.setPetOwner(user1);
        pet.setPetPhotoURL("url1");
        petRepository.save(pet);

        Message message1 = messageService.createMessage(user1, user2, pet, "Hello, I am interested in the dog.");
        messageRepository.save(message1);

        Message message2 = messageService.createMessage(user2, user1, pet, "Hi! Tell me more about the dog.");
        messageRepository.save(message2);

        List<Message> conversation = messageService.getConversation(user1.getId(), user2.getId());

        assertThat(conversation).hasSize(2);
        assertThat(conversation.get(0).getSender()).isEqualTo(user1);
        assertThat(conversation.get(1).getSender()).isEqualTo(user2);
    }
}
