package com.sideproject.cafe_cok.bookmark.dto;


import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "북마크, 북마크 폴더 ID DTO")
public class BookmarkFolderIdsDto {

    @Schema(description = "북마크 ID", example = "1")
    private Long bookmarkId;

    @Schema(description = "북마크 폴더 ID", example = "1")
    private Long folderId;

    @QueryProjection
    public BookmarkFolderIdsDto(final Long bookmarkId,
                                final Long folderId) {
        this.bookmarkId = bookmarkId;
        this.folderId = folderId;
    }
}
