package com.petshouse.petshouse.dto.user;

import lombok.Data;

@Data
public class UserPasswordUpdateRequest {
    private String newPassword;
}