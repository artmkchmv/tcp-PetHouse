package com.petshouse.petshouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    
    private String login;
    private String password;
    private String email;
    private String location;
}
