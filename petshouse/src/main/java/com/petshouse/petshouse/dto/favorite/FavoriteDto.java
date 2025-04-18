package com.petshouse.petshouse.dto.favorite;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class FavoriteDto {
    private Long id;
    private Long userId;
    private Long petId;
}
