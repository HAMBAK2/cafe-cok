package com.sideproject.cafe_cok.bookmark.dto.response;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class BookmarksResponse {

    private Long folderId;
    private String folderName;
    private String folderColor;
    private List<BookmarkDto> bookmarks = new ArrayList<>();

    public static BookmarksResponse from(final BookmarkFolder bookmarkFolder) {
        return BookmarksResponse.builder()
                .folderId(bookmarkFolder.getId())
                .folderName(bookmarkFolder.getName())
                .folderColor(bookmarkFolder.getColor())
                .build();
    }

    public static BookmarksResponse from(final BookmarkFolder bookmarkFolder, final List<Bookmark> bookmarks) {
        return BookmarksResponse.builder()
                .folderId(bookmarkFolder.getId())
                .folderName(bookmarkFolder.getName())
                .folderColor(bookmarkFolder.getColor())
                .bookmarks(BookmarkDto.fromList(bookmarks))
                .build();
    }
}
