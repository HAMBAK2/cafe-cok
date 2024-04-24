package com.sideproject.hororok.bookmark.dto;

import com.sideproject.hororok.bookmark.domain.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BookmarkDto {

    private Long bookmarkId;
    private Long cafeId;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static BookmarkDto from(Bookmark bookmark) {
        return BookmarkDto.builder()
                .bookmarkId(bookmark.getId())
                .cafeId(bookmark.getCafe().getId())
                .cafeName(bookmark.getCafe().getName())
                .roadAddress(bookmark.getCafe().getRoadAddress())
                .latitude(bookmark.getCafe().getLatitude())
                .longitude(bookmark.getCafe().getLongitude())
                .build();
    }
}
