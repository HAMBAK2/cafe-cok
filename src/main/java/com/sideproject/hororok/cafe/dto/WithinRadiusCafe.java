package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class WithinRadiusCafe {

    private final Long id;
    private final String name;
    private final String phoneNumber;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final double starRating;
    private final Long reviewCount;
    private final String imageUrl;

    public static WithinRadiusCafe of(Cafe cafe, String imageUrl) {
        return WithinRadiusCafe.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .starRating(cafe.getStarRating().doubleValue())
                .reviewCount(cafe.getReviewCount())
                .imageUrl(imageUrl)
                .build();
    }
}
