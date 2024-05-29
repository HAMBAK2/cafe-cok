package com.sideproject.cafe_cok.bookmark.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkIdResponse {

    private Long bookmarkId;

    public BookmarkIdResponse(final Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }
}
