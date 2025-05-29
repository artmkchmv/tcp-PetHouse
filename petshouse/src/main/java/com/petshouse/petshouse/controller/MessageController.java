package com.petshouse.petshouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.petshouse.petshouse.dto.message.ConversationRequest;
import com.petshouse.petshouse.dto.message.GetConversationRequest;
import com.petshouse.petshouse.dto.message.MessageDto;
import com.petshouse.petshouse.dto.message.SendMessageRequest;
import com.petshouse.petshouse.entity.Message;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.exceptions.BadRequestException;
import com.petshouse.petshouse.mapper.MessageMapper;
import com.petshouse.petshouse.security.JwtAuthentication;
import com.petshouse.petshouse.service.AuthService;
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

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody SendMessageRequest request) {
        if (request.getSenderId() == null || request.getReceiverId() == null || request.getPetId() == null) {
            throw new BadRequestException("Sender, receiver, and pet IDs must be provided");
        }

        if (request.getMessageText() == null || request.getMessageText().isBlank()) {
            throw new BadRequestException("Message text must not be empty");
        }
        
        Message message = new Message();
        message.setSender(userService.getUserById(request.getSenderId()));
        message.setReceiver(userService.getUserById(request.getReceiverId()));
        message.setPet(petService.getPetById(request.getPetId()));
        message.setMessageText(request.getMessageText());

        Message savedMessage = messageService.saveMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageMapper.toDto(savedMessage));
    }

    @GetMapping("/dialogs")
    public ResponseEntity<List<GetConversationRequest>> getDialogsForCurrentUser() {
        JwtAuthentication auth = authService.getAuthInfo();
        String login = auth.getLogin();
        User user = userService.getUserByLogin(login);

        List<GetConversationRequest> dialogs = messageService.getUserDialogs(user.getId());

        return ResponseEntity.ok(dialogs);
    }

    @PostMapping("/conversation")
    public ResponseEntity<List<MessageDto>> getConversation(@RequestBody ConversationRequest request) {
        if (request.getUserId1() == null || request.getUserId2() == null) {
            throw new BadRequestException("Both user IDs must be provided");
        }

        List<Message> conversation = messageService.getConversation(request.getUserId1(), request.getUserId2());
        List<MessageDto> messageDtos = conversation.stream()
            .map(MessageMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(messageDtos);
    }
}
