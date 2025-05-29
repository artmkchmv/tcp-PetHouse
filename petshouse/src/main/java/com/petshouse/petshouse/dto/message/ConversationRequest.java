package com.petshouse.petshouse.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationRequest {
    
    private Long userId1;
    private Long userId2;
}
