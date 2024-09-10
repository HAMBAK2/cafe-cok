package com.sideproject.cafe_cok.bookmark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 저장 응답")
public class BookmarkFolderAndBookmarkIdResponse extends RepresentationModel<BookmarkFolderAndBookmarkIdResponse> {

    @Schema(description = "저장된 북마크 ID", example = "123")
    private Long bookmarkId;

    @Schema(description = "북마크가 저장된 폴더 ID", example = "456")
    private Long bookmarkFolderId;

    public BookmarkFolderAndBookmarkIdResponse(final Long bookmarkId,
                                               final Long bookmarkFolderId) {
        this.bookmarkId = bookmarkId;
        this.bookmarkFolderId = bookmarkFolderId;
    }

}
