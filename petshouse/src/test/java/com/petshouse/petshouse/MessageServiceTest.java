package com.petshouse.petshouse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.petshouse.petshouse.entity.Message;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.repository.MessageRepository;
import com.petshouse.petshouse.repository.PetRepository;
import com.petshouse.petshouse.repository.UserRepository;
import com.petshouse.petshouse.service.MessageService;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void testSendMessage() {
        User sender = new User();
        sender.setLogin("sender");
        sender.setPassword("password123");
        sender.setEmail("sender@example.com");
        sender.setLocation("Moscow");

        User savedSender = userRepository.save(sender);

        User receiver = new User();
        receiver.setLogin("receiver");
        receiver.setPassword("password123");
        receiver.setEmail("receiver@example.com");
        receiver.setLocation("Washington");

        User savedReceiver = userRepository.save(receiver);

        Pet pet = new Pet();
        pet.setPetName("Bella");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedSender);
        pet.setPetPhotoURL("www.example.com/photo");

        Pet savedPet = petRepository.save(pet);

        Message message = new Message();
        message.setSender(savedSender);
        message.setReceiver(savedReceiver);
        message.setPet(savedPet);
        message.setMessageText("Hello, how are you?");

        Message savedMessage = messageService.sendMessage(message);

        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.getMessageId()).isNotNull();
        assertThat(savedMessage.getSender()).isEqualTo(savedSender);
        assertThat(savedMessage.getReceiver()).isEqualTo(savedReceiver);
        assertThat(savedMessage.getPet()).isEqualTo(savedPet);
        assertThat(savedMessage.getMessageText()).isEqualTo("Hello, how are you?");
    }

    @Test
    public void testGetMessagesByUser() {
        User sender = new User();
        sender.setLogin("sender");
        sender.setPassword("password123");
        sender.setEmail("sender@example.com");
        sender.setLocation("Moscow");

        User savedSender = userRepository.save(sender);

        User receiver = new User();
        receiver.setLogin("receiver");
        receiver.setPassword("password123");
        receiver.setEmail("receiver@example.com");
        receiver.setLocation("Washington");

        User savedReceiver = userRepository.save(receiver);

        Pet pet = new Pet();
        pet.setPetName("Bella");
        pet.setPetAge(3);
        pet.setPetType("Dog");
        pet.setPetDescription("Friendly dog");
        pet.setPetStatus("available");
        pet.setPetOwner(savedSender);
        pet.setPetPhotoURL("www.example.com/photo");

        Pet savedPet = petRepository.save(pet);

        Message message1 = new Message();
        message1.setSender(savedSender);
        message1.setReceiver(savedReceiver);
        message1.setPet(savedPet);
        message1.setMessageText("Hello, how are you?");

        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setSender(savedReceiver);
        message2.setReceiver(savedSender);
        message2.setPet(savedPet);
        message2.setMessageText("I'm good, thanks!");

        messageRepository.save(message2);

        List<Message> messagesForSender = messageService.getMessagesByUser(savedSender.getId());

        assertThat(messagesForSender).hasSize(2);
        assertThat(messagesForSender).usingRecursiveComparison()
        .ignoringFields("sender.registrationDate", "receiver.registrationDate", "messageTimeStamp", "pet.petOwner.registrationDate")
        .isEqualTo(List.of(message1, message2));
    }

    @Test
    public void testGetMessagesByUser_NoMessages() {
        User user = new User();
        user.setLogin("user");
        user.setPassword("password123");
        user.setEmail("user@example.com");
        user.setLocation("Moscow");
        User savedUser = userRepository.save(user);

        List<Message> messagesForUser = messageService.getMessagesByUser(savedUser.getId());

        assertThat(messagesForUser).isEmpty();
    }
}
