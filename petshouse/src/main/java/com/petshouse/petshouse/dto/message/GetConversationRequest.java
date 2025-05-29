package com.petshouse.petshouse.dto.message;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetConversationRequest {

    private Long otherUserId;
    private String otherUserLogin;
    private String lastMessageText;
    private LocalDateTime lastMessageTimestamp;
}
