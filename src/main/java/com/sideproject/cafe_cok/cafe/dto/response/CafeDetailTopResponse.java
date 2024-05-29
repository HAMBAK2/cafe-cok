package com.sideproject.cafe_cok.cafe.dto.response;


import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeDetailTopResponse {

    private Long cafeId;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal starRating;
    private Long reviewCount;
    private String originUrl;
    private String thumbnailUrl;
    private List<KeywordDto> keywords;
    private List<BookmarkIdDto> bookmarks;


    public CafeDetailTopResponse (final Cafe cafe,
                                  final Image image,
                                  final List<KeywordDto> keywords) {
        this.cafeId = cafe.getId();
        this.cafeName = cafe.getName();
        this.roadAddress = cafe.getRoadAddress();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
        this.starRating = cafe.getStarRating();
        this.reviewCount = cafe.getReviewCount();
        this.originUrl = image.getOrigin();
        this.thumbnailUrl = image.getMedium();
        this.keywords = keywords;
    }

    public CafeDetailTopResponse (final Cafe cafe,
                                  final Image image,
                                  final List<KeywordDto> keywords,
                                  final List<BookmarkIdDto> bookmarks) {
        this(cafe, image, keywords);
        this.bookmarks = bookmarks;
    }


}
