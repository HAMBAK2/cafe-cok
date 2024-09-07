package com.sideproject.cafe_cok.bookmark.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolderAndBookmarkIdResponse extends RepresentationModel<BookmarkFolderAndBookmarkIdResponse> {

    private Long bookmarkId;
    private Long bookmarkFolderId;

    public BookmarkFolderAndBookmarkIdResponse(final Long bookmarkId,
                                               final Long bookmarkFolderId) {
        this.bookmarkId = bookmarkId;
        this.bookmarkFolderId = bookmarkFolderId;
    }

}
