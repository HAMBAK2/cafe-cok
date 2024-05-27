package com.sideproject.cafe_cok.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolderCountDto {

    private Long folderId;
    private String name;
    private String color;
    private boolean isVisible;
    private boolean isDefaultFolder;
    private Integer bookmarkCount;

    @QueryProjection
    public BookmarkFolderCountDto(final Long folderId,
                                  final String name,
                                  final String color,
                                  final boolean isVisible,
                                  final boolean isDefaultFolder,
                                  final Integer bookmarkCount) {
        this.folderId = folderId;
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.isDefaultFolder = isDefaultFolder;
        this.bookmarkCount = bookmarkCount;
    }
}
