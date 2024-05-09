package com.sideproject.cafe_cok.bookmark.dto;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import lombok.Getter;

@Getter
public class BookmarkFolderDto {
    private Long folderId;
    private String name;
    private String color;
    private Long bookmarkCount;
    private boolean isVisible;
    private boolean isDefaultFolder;


    public BookmarkFolderDto(final Long folderId, final String name, final String color,
                             final Long bookmarkCount, final boolean isVisible, final boolean isDefaultFolder) {
        this.folderId = folderId;
        this.name = name;
        this.color = color;
        this.bookmarkCount = bookmarkCount;
        this.isVisible = isVisible;
        this.isDefaultFolder = isDefaultFolder;
    }

    public BookmarkFolderDto(final BookmarkFolder folder, final Long bookmarkCount) {
        this.folderId = folder.getId();
        this.name = folder.getName();
        this.color = folder.getColor();
        this.bookmarkCount = bookmarkCount;
        this.isVisible = folder.getIsVisible();
        this.isDefaultFolder = folder.getIsDefaultFolder();

    }
}
