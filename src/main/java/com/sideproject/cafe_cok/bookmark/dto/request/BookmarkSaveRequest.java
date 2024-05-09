package com.sideproject.cafe_cok.bookmark.dto.request;

import lombok.Getter;

@Getter
public class BookmarkSaveRequest {

    private Long cafeId;
    private Long folderId;

    private BookmarkSaveRequest() {
    }

    public BookmarkSaveRequest(final Long cafeId, final Long folderId) {
        this.cafeId = cafeId;
        this.folderId = folderId;
    }
}
