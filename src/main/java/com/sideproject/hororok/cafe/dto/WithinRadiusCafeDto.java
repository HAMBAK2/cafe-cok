package com.sideproject.hororok.cafe.dto;


import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class WithinRadiusCafeDto {

    private final Long id;
    private final String name;
    private final String phoneNumber;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private double starRating;
    private Long reviewCount;
    private String imageUrl;

    public static WithinRadiusCafeDto of(Cafe cafe, String imageUrl) {
        return WithinRadiusCafeDto.builder()
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
