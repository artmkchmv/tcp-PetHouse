package com.petshouse.petshouse.dto.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {
    
    public String refreshToken;
}
