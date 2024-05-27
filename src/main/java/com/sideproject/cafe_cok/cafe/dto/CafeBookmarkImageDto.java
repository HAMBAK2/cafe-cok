package com.sideproject.cafe_cok.cafe.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeBookmarkImageDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal starRating;
    private Long reviewCount;
    private String imageUrl;
    private Long bookmarkId;

    @QueryProjection
    public CafeBookmarkImageDto(final Long id,
                                final String name,
                                final String phoneNumber,
                                final String roadAddress,
                                final BigDecimal latitude,
                                final BigDecimal longitude,
                                final BigDecimal starRating,
                                final Long reviewCount,
                                final String imageUrl,
                                final Long bookmarkId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.bookmarkId = bookmarkId;
    }
}
