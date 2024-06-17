package com.sideproject.cafe_cok.bookmark.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolderAndBookmarkIdResponse {

    private Long bookmarkId;
    private Long bookmarkFolderId;

    public BookmarkFolderAndBookmarkIdResponse(final Long bookmarkId,
                                               final Long bookmarkFolderId) {
        this.bookmarkId = bookmarkId;
        this.bookmarkFolderId = bookmarkFolderId;
    }

}
