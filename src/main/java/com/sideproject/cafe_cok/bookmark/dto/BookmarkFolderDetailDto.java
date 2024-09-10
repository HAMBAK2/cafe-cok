package com.sideproject.cafe_cok.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 폴더 상세 정보")
public class BookmarkFolderDetailDto {

    @Schema(description = "북마크 폴더 ID", example = "1")
    private Long folderId;

    @Schema(description = "북마크 폴더 이름", example = "폴더 이름")
    private String name;

    @Schema(description = "북마크 폴더 색상", example = "RED")
    private String color;

    @Schema(description = "북마크 폴더 지도 노출 여부")
    private boolean isVisible;

    @Schema(description = "북마크 폴더 기본 폴더 여부")
    private boolean isDefaultFolder;

    @Schema(description = "북마크 폴더에 저장된 북마크 개수", example = "4")
    private Long bookmarkCount;

    @QueryProjection
    public BookmarkFolderDetailDto(final Long folderId,
                                   final String name,
                                   final String color,
                                   final boolean isVisible,
                                   final boolean isDefaultFolder,
                                   final Long bookmarkCount) {
        this.folderId = folderId;
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
        this.isDefaultFolder = isDefaultFolder;
        this.bookmarkCount = bookmarkCount;
    }
}
