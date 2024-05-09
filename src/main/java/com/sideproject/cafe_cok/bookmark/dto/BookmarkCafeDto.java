package com.sideproject.cafe_cok.bookmark.dto;


import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BookmarkCafeDto {

    private Long bookmarkId;
    private Long folderId;


    public static BookmarkCafeDto from(final Bookmark bookmark) {
        return BookmarkCafeDto.builder()
                .bookmarkId(bookmark.getId())
                .folderId(bookmark.getBookmarkFolder().getId())
                .build();
    }

    public static List<BookmarkCafeDto> fromList(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> BookmarkCafeDto.from(bookmark))
                .collect(Collectors.toList());
    }

}
