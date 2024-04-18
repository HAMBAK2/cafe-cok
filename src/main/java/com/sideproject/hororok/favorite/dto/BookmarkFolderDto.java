package com.sideproject.hororok.favorite.dto;

import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkFolderDto {
    private Long folderId;
    private String name;
    private String color;
    private Long favoriteCount;
    private boolean isVisible;

    public static BookmarkFolderDto of(final BookmarkFolder bookmarkFolder, final Long favoriteCount) {
        return BookmarkFolderDto.builder()
                .folderId(bookmarkFolder.getId())
                .name(bookmarkFolder.getName())
                .color(bookmarkFolder.getColor())
                .favoriteCount(favoriteCount)
                .isVisible(bookmarkFolder.getIsVisible())
                .build();
    }

    public static BookmarkFolderDto of(final Long folderId, final String name, final String color,
                                       final boolean isVisible, final Long favoriteCount) {
        return BookmarkFolderDto.builder()
                .folderId(folderId)
                .name(name)
                .color(color)
                .favoriteCount(favoriteCount)
                .isVisible(isVisible)
                .build();
    }
}
