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
    private Long bookmarkCount;
    private boolean isVisible;
    private boolean isDefaultFolder;

    public static BookmarkFolderDto of(final BookmarkFolder bookmarkFolder, final Long bookmarkCount) {
        return BookmarkFolderDto.builder()
                .folderId(bookmarkFolder.getId())
                .name(bookmarkFolder.getName())
                .color(bookmarkFolder.getColor())
                .bookmarkCount(bookmarkCount)
                .isVisible(bookmarkFolder.getIsVisible())
                .isDefaultFolder(bookmarkFolder.getIsDefaultFolder())
                .build();
    }

    public static BookmarkFolderDto of(final Long folderId, final String name, final String color,
                                       final boolean isVisible, final boolean isDefaultFolder,
                                       final Long bookmarkCount) {
        return BookmarkFolderDto.builder()
                .folderId(folderId)
                .name(name)
                .color(color)
                .bookmarkCount(bookmarkCount)
                .isVisible(isVisible)
                .isDefaultFolder(isDefaultFolder)
                .build();
    }
}
