package com.sideproject.hororok.favorite.dto.request;

import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.member.domain.Member;
import lombok.Getter;

@Getter
public class BookmarkFolderSaveRequest {

    private String name;
    private String color;
    private Boolean isVisible;

    private BookmarkFolderSaveRequest() {
    }

    public BookmarkFolderSaveRequest(final String name,
                                     final String color,
                                     final Boolean isVisible) {
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
    }

    public BookmarkFolder toBookmarkFolder(Member member) {
        return new BookmarkFolder(
                this.name, this.color,
                this.isVisible, false, member);
    }
}
