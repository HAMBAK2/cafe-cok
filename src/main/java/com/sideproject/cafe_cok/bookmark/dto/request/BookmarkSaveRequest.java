package com.sideproject.cafe_cok.bookmark.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 저장 요청")
public class BookmarkSaveRequest {

    @Schema(description = "북마크하려는 카페의 ID", example = "123")
    private Long cafeId;

    @Schema(description = "북마크를 저장할 폴더의 ID", example = "456")
    private Long folderId;

    public BookmarkSaveRequest(final Long cafeId, final Long folderId) {
        this.cafeId = cafeId;
        this.folderId = folderId;
    }
}
