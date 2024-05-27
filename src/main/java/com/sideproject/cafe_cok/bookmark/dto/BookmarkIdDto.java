package com.sideproject.cafe_cok.bookmark.dto;


import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BookmarkIdDto {

    private Long bookmarkId;
    private Long folderId;


    public static BookmarkIdDto from(final Bookmark bookmark) {
        return BookmarkIdDto.builder()
                .bookmarkId(bookmark.getId())
                .folderId(bookmark.getBookmarkFolder().getId())
                .build();
    }

    public static List<BookmarkIdDto> fromList(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> BookmarkIdDto.from(bookmark))
                .collect(Collectors.toList());
    }

}
