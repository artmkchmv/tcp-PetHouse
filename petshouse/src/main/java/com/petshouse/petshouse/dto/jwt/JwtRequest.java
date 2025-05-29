package com.petshouse.petshouse.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    
    private String login;
    private String password;
}
