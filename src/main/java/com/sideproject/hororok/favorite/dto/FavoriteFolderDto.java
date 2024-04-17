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
    private Long favoriteCount;
    private boolean isVisible;

    public static FavoriteFolderDto of(final FavoriteFolder favoriteFolder, final Long favoriteCount) {
        return FavoriteFolderDto.builder()
                .folderId(favoriteFolder.getId())
                .name(favoriteFolder.getName())
                .color(favoriteFolder.getColor())
                .favoriteCount(favoriteCount)
                .isVisible(favoriteFolder.getIsVisible())
                .build();
    }

    public static FavoriteFolderDto of(final Long folderId, final String name, final String color,
                                       final boolean isVisible, final Long favoriteCount) {
        return FavoriteFolderDto.builder()
                .folderId(folderId)
                .name(name)
                .color(color)
                .favoriteCount(favoriteCount)
                .isVisible(isVisible)
                .build();
    }
}
