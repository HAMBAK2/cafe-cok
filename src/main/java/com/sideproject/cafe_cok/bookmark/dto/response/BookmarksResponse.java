package com.sideproject.cafe_cok.bookmark.dto.response;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkCafeDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarksResponse extends RepresentationModel<BookmarksResponse> {

    private Long folderId;
    private String folderName;
    private String folderColor;
    private List<BookmarkCafeDto> bookmarks = new ArrayList<>();

    public BookmarksResponse (final BookmarkFolder bookmarkFolder,
                              final List<BookmarkCafeDto> bookmarks) {
        this.folderId = bookmarkFolder.getId();
        this.folderName = bookmarkFolder.getName();
        this.folderColor = bookmarkFolder.getColor();
        this.bookmarks = bookmarks;
    }
}
