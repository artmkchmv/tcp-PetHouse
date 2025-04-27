package com.petshouse.petshouse.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FavoriteDto {
    private Long id;
    private Long userId;
    private Long petId;
}
