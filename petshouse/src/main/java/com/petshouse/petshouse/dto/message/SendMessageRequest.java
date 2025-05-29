package com.petshouse.petshouse.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    private Long senderId;
    private Long receiverId;
    private Long petId;
    private String messageText;
}
