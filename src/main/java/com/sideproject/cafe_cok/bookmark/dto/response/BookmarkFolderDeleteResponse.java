package com.sideproject.cafe_cok.bookmark.dto.response;

import lombok.Getter;

@Getter
public class BookmarkFolderDeleteResponse {

    private Long folderId;

    public BookmarkFolderDeleteResponse(final Long folderId) {
        this.folderId = folderId;
    }
}
