package com.petshouse.petshouse.mapper;

import com.petshouse.petshouse.dto.user.UserDto;
import com.petshouse.petshouse.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin()); 
        dto.setEmail(user.getEmail());
        dto.setLocation(user.getLocation());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }
}
