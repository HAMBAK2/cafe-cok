package com.sideproject.hororok.favorite.dto;

import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteFolderDto {
    private Long folderId;
    private String name;
    private String color;
    private boolean isVisible;

    public static FavoriteFolderDto from(FavoriteFolder favoriteFolder) {
        return FavoriteFolderDto.builder()
                .folderId(favoriteFolder.getId())
                .name(favoriteFolder.getName())
                .color(favoriteFolder.getColor())
                .isVisible(favoriteFolder.getIsVisible())
                .build();
    }

    public static FavoriteFolderDto of(final Long folderId, final String name,
                                       final String color, final boolean isVisible) {
        return FavoriteFolderDto.builder()
                .folderId(folderId)
                .name(name)
                .color(color)
                .isVisible(isVisible)
                .build();
    }
}
