package com.petshouse.petshouse.enums;

import org.springframework.security.core.GrantedAuthority;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {

    ADMIN("Admin"),
    USER("User");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
