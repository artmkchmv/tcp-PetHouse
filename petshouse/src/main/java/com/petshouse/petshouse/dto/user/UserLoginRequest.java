package com.petshouse.petshouse.dto.user;

import lombok.Data;

@Data
public class UserLoginRequest {
    
    private String login;
    private String password;
}
