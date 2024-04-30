package com.sideproject.hororok.cafe.dto.response;


import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.dto.KeywordDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CafeDetailTopResponse {

    private Long cafeId;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double starRating;
    private Long reviewCount;
    private String imageUrl;
    private List<KeywordDto> keywords;

    public static CafeDetailTopResponse of(final Cafe cafe, final String imageUrl,
                                           final Long reviewCount, final List<KeywordDto> keywords) {
        return CafeDetailTopResponse.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(reviewCount)
                .imageUrl(imageUrl)
                .keywords(keywords)
                .build();
    }

}
