package com.petshouse.petshouse.dto.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {
    
    private String login;
    private String password;
}
