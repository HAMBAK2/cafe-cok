package com.sideproject.cafe_cok.bookmark.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 폴더 수정 요청")
public class BookmarkFolderUpdateRequest {

    @Schema(description = "북마크 폴더 ID", example = "1")
    private Long folderId;

    @Schema(description = "북마크 폴더 이름", example = "폴더 이름")
    private String name;

    @Schema(description = "북마크 폴더 색상", example = "RED")
    private String color;

    @Schema(description = "북마크 폴더 지도 노출 여부")
    private Boolean isVisible;

    public BookmarkFolderUpdateRequest(final Long folderId,
                                       final String name,
                                       final String color,
                                       final Boolean isVisible) {
        this.folderId = folderId;
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
    }
}
