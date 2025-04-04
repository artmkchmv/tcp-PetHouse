package com.petshouse.petshouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petshouse.petshouse.entity.Message;
import com.petshouse.petshouse.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByUser(Long userId) {
        return messageRepository.findBySender_IdOrReceiver_Id(userId, userId);
    }
}