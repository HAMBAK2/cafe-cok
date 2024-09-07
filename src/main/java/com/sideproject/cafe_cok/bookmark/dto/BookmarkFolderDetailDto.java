package com.sideproject.cafe_cok.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolderDetailDto {

    private Long folderId;
    private String name;
    private String color;
    private boolean isVisible;
    private boolean isDefaultFolder;
    private Long bookmarkCount;

    @QueryProjection
    public BookmarkFolderDetailDto(final Long folderId,
                                   final String name,
                                   final String color,
                                   final boolean isVisible,
                                   final boolean isDefaultFolder,
                                   final Long bookmarkCount) {
        this.folderId = folderId;
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.isDefaultFolder = isDefaultFolder;
        this.bookmarkCount = bookmarkCount;
    }
}
