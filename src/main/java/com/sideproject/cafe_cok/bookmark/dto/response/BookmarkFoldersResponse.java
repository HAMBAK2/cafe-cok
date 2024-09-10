package com.sideproject.cafe_cok.bookmark.dto.response;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 폴더 목록 조회 응답")
public class BookmarkFoldersResponse extends RepresentationModel<BookmarkFoldersResponse> {

    @Schema(description = "폴더의 개수", example = "4")
    private Integer folderCount;

    @Schema(description = "각 폴더의 상세 정보 목록")
    private List<BookmarkFolderDetailDto> folders;

    public BookmarkFoldersResponse(final List<BookmarkFolderDetailDto> folders) {
        this.folderCount = folders.size();
        this.folders = folders;
    }
}
