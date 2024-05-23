package com.sideproject.cafe_cok.bookmark.dto.request;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkFolderSaveRequest {

    private String name;
    private String color;
    private Boolean isVisible;

    public BookmarkFolderSaveRequest(final String name,
                                     final String color,
                                     final Boolean isVisible) {
        this.name = name;
        this.color = color;
        this.isVisible = isVisible;
    }

    public BookmarkFolder toBookmarkFolder(final Member member) {
        return new BookmarkFolder(
                this.name, this.color,
                this.isVisible, false, member);
    }
}
