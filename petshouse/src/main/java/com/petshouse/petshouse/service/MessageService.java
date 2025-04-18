package com.petshouse.petshouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message createMessage(
            User messageSender,
            User messageReceiver,
            Pet messagePet,
            String messageText
            ) {
        Message message = new Message();
        message.setSender(messageSender);
        message.setReceiver(messageReceiver);
        message.setPet(messagePet);
        message.setMessageText(messageText);
        return message;
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversationBetweenUsers(userId1, userId2);
    }
}
