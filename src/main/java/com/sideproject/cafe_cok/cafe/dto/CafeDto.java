package com.sideproject.cafe_cok.cafe.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal starRating;
    private Long reviewCount;
    private String imageUrl;
    private List<BookmarkFolderIdsDto> bookmarks;


    @Builder
    @QueryProjection
    public CafeDto(final Long id,
                   final String name,
                   final String phoneNumber,
                   final String roadAddress,
                   final BigDecimal latitude,
                   final BigDecimal longitude,
                   final BigDecimal starRating,
                   final Long reviewCount,
                   final String imageUrl,
                   final List<BookmarkFolderIdsDto> bookmarks) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.bookmarks = bookmarks;
    }

    public void setBookmarks(final List<BookmarkFolderIdsDto> bookmarks) {
        this.bookmarks = bookmarks;
    }

}
