package com.sideproject.cafe_cok.cafe.dto.response;


import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class CafeTopResponse extends RepresentationModel<CafeTopResponse> {

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
    private List<BookmarkFolderIdsDto> bookmarks;

    @Builder
    public CafeTopResponse(final Long cafeId,
                           final String cafeName,
                           final String roadAddress,
                           final BigDecimal latitude,
                           final BigDecimal longitude,
                           final BigDecimal starRating,
                           final Long reviewCount,
                           final String originUrl,
                           final String thumbnailUrl,
                           final List<KeywordDto> keywords,
                           final List<BookmarkFolderIdsDto> bookmarks) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.starRating = starRating;
        this.reviewCount = reviewCount;
        this.originUrl = originUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.keywords = keywords;
        this.bookmarks = bookmarks;
    }
}
