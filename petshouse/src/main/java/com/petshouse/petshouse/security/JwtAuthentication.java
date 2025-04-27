package com.petshouse.petshouse.security;

import java.util.Collection;
import java.util.Collections;

import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.petshouse.petshouse.enums.UserRole;

@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String login;
    private String email;
    private String location;
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return login; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return login; }
}
