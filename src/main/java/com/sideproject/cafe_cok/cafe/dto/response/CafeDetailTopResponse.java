package com.sideproject.cafe_cok.cafe.dto.response;


import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CafeDetailTopResponse {

    private Long cafeId;
    private List<BookmarkIdDto> bookmarks;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double starRating;
    private Long reviewCount;
    private String originUrl;
    private String thumbnailUrl;
    private List<KeywordDto> keywords;

    public static CafeDetailTopResponse of(final Cafe cafe, final Image image,
                                           final Long reviewCount, final List<KeywordDto> keywords) {
        return CafeDetailTopResponse.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .starRating(cafe.getStarRating().doubleValue())
                .originUrl(image.getOrigin())
                .thumbnailUrl(image.getMedium())
                .reviewCount(reviewCount)
                .keywords(keywords)
                .build();
    }

    public static CafeDetailTopResponse of(final Cafe cafe, final List<BookmarkIdDto> bookmarks, final Image image,
                                           final Long reviewCount, final List<KeywordDto> keywords) {
        return CafeDetailTopResponse.builder()
                .cafeId(cafe.getId())
                .bookmarks(bookmarks)
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(reviewCount)
                .originUrl(image.getOrigin())
                .thumbnailUrl(image.getMedium())
                .keywords(keywords)
                .build();
    }

}
