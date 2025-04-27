package com.petshouse.petshouse.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.petshouse.petshouse.enums.UserRole;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setLogin(claims.getSubject());
        jwtInfoToken.setEmail(claims.get("email", String.class));
        jwtInfoToken.setRole(getRole(claims));
        return jwtInfoToken;
    }
    
    private static UserRole getRole(Claims claims) {
        final String role = claims.get("role", String.class);
        return role != null ? UserRole.valueOf(role) : null;
    }
}
