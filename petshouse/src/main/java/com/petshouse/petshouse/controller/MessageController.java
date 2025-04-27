package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.message.MessageDto;
import com.petshouse.petshouse.entity.Message;
import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.service.MessageService;
import com.petshouse.petshouse.service.PetService;
import com.petshouse.petshouse.service.UserService;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final UserService userService;

    private final PetService petService;

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
}
