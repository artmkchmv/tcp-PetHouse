package com.petshouse.petshouse.mapper;

import com.petshouse.petshouse.dto.message.GetConversationRequest;
import com.petshouse.petshouse.dto.message.MessageDto;
import com.petshouse.petshouse.entity.Message;
import com.petshouse.petshouse.entity.User;

public class MessageMapper {

    private MessageMapper() {}

    public static MessageDto toDto(Message message) {
        if (message == null) {
            return null;
        }
        MessageDto dto = new MessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setSenderId(message.getSender() != null ? message.getSender().getId() : null);
        dto.setReceiverId(message.getReceiver() != null ? message.getReceiver().getId() : null);
        dto.setPetId(message.getPet() != null ? message.getPet().getPetId() : null);
        dto.setMessageText(message.getMessageText());
        dto.setMessageTimeStamp(message.getMessageTimeStamp());
        return dto;
    }

    public static GetConversationRequest toGetConversationRequest(User otherUser, Message lastMessage) {
        if (otherUser == null || lastMessage == null) {
            return null;
        }
        GetConversationRequest dto = new GetConversationRequest();
        dto.setOtherUserId(otherUser.getId());
        dto.setOtherUserLogin(otherUser.getLogin());
        dto.setLastMessageText(lastMessage.getMessageText());
        dto.setLastMessageTimestamp(lastMessage.getMessageTimeStamp());
        return dto;
    }
}
