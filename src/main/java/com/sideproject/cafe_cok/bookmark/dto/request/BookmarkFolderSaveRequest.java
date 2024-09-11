package com.sideproject.cafe_cok.bookmark.dto.request;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 폴더 저장 요청")
public class BookmarkFolderSaveRequest {

    @Schema(description = "북마크 폴더 이름", example = "폴더 이름")
    private String name;

    @Schema(description = "북마크 폴더 색상", example = "RED")
    private String color;

    @Schema(description = "북마크 폴더 지도 노출 여부")
    private Boolean isVisible;

    public BookmarkFolderSaveRequest(final String name,
                                     final String color,
                                     final Boolean isVisible) {
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
    }

    public BookmarkFolder toEntity(final Member member) {

        return BookmarkFolder.builder()
                .name(this.name)
                .color(this.color)
                .isVisible(this.isVisible)
                .isDefaultFolder(false)
                .member(member)
                .build();
    }
}
