package com.sideproject.hororok.bookmark.dto.response;

import com.sideproject.hororok.bookmark.dto.BookmarkDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookmarksResponse {

    private Long folderId;
    private String folderName;
    private String folderColor;
    private List<BookmarkDto> bookmarks = new ArrayList<>();


    private BookmarksResponse() {
    }

    public BookmarksResponse(
            final Long folderId, final String folderName, final String folderColor) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.folderColor = folderColor;
        this.bookmarks = new ArrayList<>();
    }

    public BookmarksResponse(
            final Long folderId, final String folderName,
            final String folderColor, final List<BookmarkDto> bookmarks) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.folderColor = folderColor;
        this.bookmarks = bookmarks;
    }
}
