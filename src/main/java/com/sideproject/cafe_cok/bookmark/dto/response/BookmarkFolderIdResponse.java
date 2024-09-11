package com.sideproject.cafe_cok.bookmark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 폴더 ID 응답")
public class BookmarkFolderIdResponse extends RepresentationModel<BookmarkFolderIdResponse> {

    @Schema(description = "북마크 폴더 ID", example = "1")
    private Long folderId;

    public BookmarkFolderIdResponse(final Long folderId) {
        this.folderId = folderId;
    }
}
