package com.sideproject.cafe_cok.bookmark.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolderIdsDto {

    private Long bookmarkId;
    private Long folderId;

    @QueryProjection
    public BookmarkFolderIdsDto(final Long bookmarkId,
                                final Long folderId) {
        this.bookmarkId = bookmarkId;
        this.folderId = folderId;
    }
}
