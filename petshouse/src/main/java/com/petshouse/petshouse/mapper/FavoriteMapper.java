package com.petshouse.petshouse.mapper;

import com.petshouse.petshouse.dto.favorite.FavoriteDto;
import com.petshouse.petshouse.entity.Favorite;

public class FavoriteMapper {

    private FavoriteMapper() {}

    public static FavoriteDto toDto(Favorite favorite) {
        if (favorite == null) {
            return null;
        }
        FavoriteDto dto = new FavoriteDto();
        dto.setFavId(favorite.getId());
        dto.setUserId(favorite.getUser().getId());
        dto.setPetId(favorite.getPet().getPetId());
        return dto;
    }

}
