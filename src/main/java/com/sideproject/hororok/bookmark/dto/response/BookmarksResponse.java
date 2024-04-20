package com.sideproject.hororok.bookmark.dto.response;

import com.sideproject.hororok.bookmark.dto.BookmarkDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookmarksResponse {

    private Long folderId;
    private String folderName;
    private String color;
    private List<BookmarkDto> bookmarks = new ArrayList<>();


    private BookmarksResponse() {
    }

    public BookmarksResponse(
            final Long folderId, final String folderName, final String color) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.color = color;
        this.bookmarks = new ArrayList<>();
    }

    public BookmarksResponse(
            final Long folderId, final String folderName,
            final String color, final List<BookmarkDto> bookmarks) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.color = color;
        this.bookmarks = bookmarks;
    }
}
