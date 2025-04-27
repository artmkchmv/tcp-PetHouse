package com.petshouse.petshouse.mapper;

import com.petshouse.petshouse.dto.user.UserDto;
import com.petshouse.petshouse.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
            user.getId(),
            user.getLogin(),
            user.getEmail(),
            user.getLocation(),
            user.getRegistrationDate()
        );
    }
}
