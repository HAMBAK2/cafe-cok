package com.sideproject.cafe_cok.bookmark.dto.response;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkCafeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "북마크 폴더 조회 응답")
public class BookmarksResponse extends RepresentationModel<BookmarksResponse> {

    @Schema(description = "북마크 폴더 ID", example = "1")
    private Long folderId;

    @Schema(description = "북마크 폴더 이름", example = "폴더 이름")
    private String folderName;

    @Schema(description = "북마크 폴더 색상", example = "RED")
    private String folderColor;

    @Schema(description = "북마크로 지정한 카페의 목록")
    private List<BookmarkCafeDto> bookmarks = new ArrayList<>();

    public BookmarksResponse (final BookmarkFolder bookmarkFolder,
                              final List<BookmarkCafeDto> bookmarks) {
        this.folderId = bookmarkFolder.getId();
        this.folderName = bookmarkFolder.getName();
        this.folderColor = bookmarkFolder.getColor();
        this.bookmarks = bookmarks;
    }
}
