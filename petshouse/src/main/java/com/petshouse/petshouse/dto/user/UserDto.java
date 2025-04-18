package com.petshouse.petshouse.dto.user;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String login;
    private String email;
    private String location;
    private LocalDateTime registrationDate;
}
