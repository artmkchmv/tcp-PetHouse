package com.petshouse.petshouse.dto.message;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private Long petId;
    private String messageText;
    private LocalDateTime messageTimeStamp;
}
