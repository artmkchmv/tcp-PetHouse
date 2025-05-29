package com.petshouse.petshouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailUpdateRequest {
    
    private Long id;
    private String newEmail;
}