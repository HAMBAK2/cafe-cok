package com.sideproject.cafe_cok.bookmark.dto;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BookmarkFolderDto {
    private Long folderId;
    private String name;
    private String color;
    private boolean isVisible;
    private boolean isDefaultFolder;
    private Integer bookmarkCount;

    public static BookmarkFolderDto from(final BookmarkFolder bookmarkFolder) {
        return BookmarkFolderDto.builder()
                .folderId(bookmarkFolder.getId())
                .name(bookmarkFolder.getName())
                .color(bookmarkFolder.getColor())
                .bookmarkCount(bookmarkFolder.getBookmarks().size())
                .isVisible(bookmarkFolder.getIsVisible())
                .isDefaultFolder(bookmarkFolder.getIsDefaultFolder())
                .build();
    }

    public static List<BookmarkFolderDto> fromList(final List<BookmarkFolder> bookmarkFolders) {
        return bookmarkFolders.stream()
                .map(bookmarkFolder -> BookmarkFolderDto.from(bookmarkFolder))
                .collect(Collectors.toList());
    }
}
