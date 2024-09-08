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
