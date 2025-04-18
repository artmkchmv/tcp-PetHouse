package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.petshouse.petshouse.dto.message.MessageDto;
import com.petshouse.petshouse.entity.*;
import com.petshouse.petshouse.service.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private MessageDto toDto(Message message) {
        return new MessageDto(
            message.getMessageId(),
            message.getSender().getId(),
            message.getReceiver().getId(),
            message.getPet().getPetId(),
            message.getMessageText(),
            message.getMessageTimeStamp()
        );
    }

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestParam Long senderId, 
                                                  @RequestParam Long receiverId, 
                                                  @RequestParam Long petId, 
                                                  @RequestParam String messageText) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(receiverId);
        Pet pet = petService.getPetById(petId);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setPet(pet);
        message.setMessageText(messageText);

        Message savedMessage = messageService.saveMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedMessage));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDto>> getConversation(@RequestParam Long userId1, @RequestParam Long userId2) {
        List<Message> conversation = messageService.getConversation(userId1, userId2);
        List<MessageDto> messageDtos = conversation.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(messageDtos);
    }
}
