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
import com.petshouse.petshouse.enums.PetStatus;
import com.petshouse.petshouse.enums.PetType;
import com.petshouse.petshouse.repository.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class MessageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    public Message createMessage(User messageSender, User messageReceiver, Pet messagePet, String messageText) {
        Message message = new Message();
        message.setSender(messageSender);
        message.setReceiver(messageReceiver);
        message.setPet(messagePet);
        message.setMessageText(messageText);
        return message;
    }

    @Test
    public void testFindConversationBetweenUsers() {
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

        Message message1 = createMessage(user1, user2, pet, "Hello, I am interested in the dog.");
        messageRepository.save(message1);

        Message message2 = createMessage(user2, user1, pet, "Hi! Tell me more about the dog.");
        messageRepository.save(message2);

        List<Message> conversation = messageRepository.findConversationBetweenUsers(user1.getId(), user2.getId());

        assertThat(conversation).hasSize(2);
        assertThat(conversation.get(0).getSender()).isEqualTo(user1);
        assertThat(conversation.get(1).getSender()).isEqualTo(user2);
    }

    @Test
    public void testFindConversationNoMessages() {
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

        List<Message> conversation = messageRepository.findConversationBetweenUsers(user1.getId(), user2.getId());

        assertThat(conversation).isEmpty();
    }

    @Test
    public void testFindConversationMultipleMessages() {
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

        Message message1 = createMessage(user1, user2, pet, "Hello, I am interested in the dog.");
        messageRepository.save(message1);

        Message message2 = createMessage(user2, user1, pet, "Hi! Tell me more about the dog.");
        messageRepository.save(message2);

        Message message3 = createMessage(user1, user2, pet, "Can I meet the dog?");
        messageRepository.save(message3);

        List<Message> conversation = messageRepository.findConversationBetweenUsers(user1.getId(), user2.getId());

        assertThat(conversation).hasSize(3);
        assertThat(conversation.get(0).getSender()).isEqualTo(user1);
        assertThat(conversation.get(1).getSender()).isEqualTo(user2);
        assertThat(conversation.get(2).getSender()).isEqualTo(user1);
    }

    @Test
    public void testFindConversationBetweenUsersWithSameSenderReceiver() {
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

        Message message = createMessage(user1, user1, pet, "Is the dog still available?");
        messageRepository.save(message);

        List<Message> conversation = messageRepository.findConversationBetweenUsers(user1.getId(), user2.getId());
        assertThat(conversation).isEmpty();
    }
}
