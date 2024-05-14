package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkCafeDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CafeDto {

    private Long id;
    private List<BookmarkCafeDto> bookmarks;
    private String name;
    private String phoneNumber;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double starRating;
    private Long reviewCount;
    private String imageUrl;

    public static CafeDto from(final Cafe cafe) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(cafe.getReviewCount())
                .imageUrl(cafe.getMainImage())
                .build();
    }

    public static CafeDto of(final Cafe cafe, final String imageUrl) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(cafe.getReviewCount())
                .imageUrl(imageUrl)
                .build();
    }

    public void setBookmarks(final List<BookmarkCafeDto> bookmarks) {
        this.bookmarks = bookmarks;
    }

}
