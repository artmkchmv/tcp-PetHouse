package com.petshouse.petshouse.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDto {
    
    private Long favId;
    private Long userId;
    private Long petId;
}
