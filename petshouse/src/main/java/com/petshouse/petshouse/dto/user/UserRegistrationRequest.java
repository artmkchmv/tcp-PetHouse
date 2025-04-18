package com.petshouse.petshouse.dto.user;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String login;
    private String password;
    private String email;
    private String location;
}
