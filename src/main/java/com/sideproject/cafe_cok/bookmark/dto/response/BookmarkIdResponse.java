package com.sideproject.cafe_cok.bookmark.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkIdResponse {

    private Long bookmarkId;

    public static BookmarkIdResponse of(final Long bookmarkId){
        return BookmarkIdResponse.builder()
                .bookmarkId(bookmarkId)
                .build();
    }
}
