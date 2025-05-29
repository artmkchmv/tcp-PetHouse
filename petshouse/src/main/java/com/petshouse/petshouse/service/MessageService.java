package com.petshouse.petshouse.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.message.GetConversationRequest;
import com.petshouse.petshouse.entity.Message;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message createMessage(
            User messageSender,
            User messageReceiver,
            Pet messagePet,
            String messageText
            ) {
                
        if (messageSender == null) {
            throw new BadRequestException("Message sender must not be null");
        }
        if (messageReceiver == null) {
            throw new BadRequestException("Message receiver must not be null");
        }
        if (messagePet == null) {
            throw new BadRequestException("Associated pet must not be null");
        }
        if (messageText == null || messageText.isBlank()) {
            throw new BadRequestException("Message text must not be empty");
        }

        Message message = new Message();
        message.setSender(messageSender);
        message.setReceiver(messageReceiver);
        message.setPet(messagePet);
        message.setMessageText(messageText);
        return message;
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        if (userId1 == null || userId2 == null) {
            throw new BadRequestException("User IDs must not be null");
        }
        return messageRepository.findConversationBetweenUsers(userId1, userId2);
    }

    public List<Message> getAllMessagesByUser(Long userId) {
        if (userId == null) {
            throw new BadRequestException("User ID must not be null");
        }
        return messageRepository.findAllByUserId(userId);
    }

    public List<GetConversationRequest> getUserDialogs(Long userId) {
        if (userId == null) {
            throw new BadRequestException("User ID must not be null");
        }

        List<Object[]> rawDialogs = messageRepository.findDialogsByUserId(userId);

        return rawDialogs.stream()
            .map(record -> new GetConversationRequest(
                (Long) record[0],
                (String) record[1],
                (String) record[2],
                (LocalDateTime) record[3]
            ))
            .collect(Collectors.toList());
    }
}
