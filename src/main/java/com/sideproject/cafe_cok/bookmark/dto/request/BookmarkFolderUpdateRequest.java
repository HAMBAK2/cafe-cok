package com.sideproject.cafe_cok.bookmark.dto.request;

import lombok.Getter;

@Getter
public class BookmarkFolderUpdateRequest {

    private Long folderId;
    private String name;
    private String color;
    private Boolean isVisible;

    private BookmarkFolderUpdateRequest() {
    }

    public BookmarkFolderUpdateRequest(
            final Long folderId, final String name,
            final String color, final Boolean isVisible) {
        this.folderId = folderId;
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
    }
}
