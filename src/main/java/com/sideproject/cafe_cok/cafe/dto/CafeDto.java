package com.sideproject.cafe_cok.cafe.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.AccessLevel;
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
    private List<BookmarkIdDto> bookmarks;

    @QueryProjection
    public CafeDto(final Cafe cafe,
                   final String imageUrl,
                   final List<BookmarkIdDto> bookmarks) {
        this.id = cafe.getId();
        this.name = cafe.getName();
        this.phoneNumber = cafe.getPhoneNumber();
        this.roadAddress = cafe.getRoadAddress();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
        this.starRating = cafe.getStarRating();
        this.reviewCount = cafe.getReviewCount();
        this.imageUrl = imageUrl;
        this.bookmarks = bookmarks;
    }

    public void setBookmarks(final List<BookmarkIdDto> bookmarks) {
        this.bookmarks = bookmarks;
    }

}
