package com.sideproject.cafe_cok.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkCafeDto {

    private Long bookmarkId;
    private Long cafeId;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public BookmarkCafeDto(final Long bookmarkId,
                           final Cafe cafe) {
        this.bookmarkId = bookmarkId;
        this.cafeId = cafe.getId();
        this.cafeName = cafe.getName();
        this.roadAddress = cafe.getRoadAddress();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
    }
}
