package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CafeDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String roadAddress;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private double starRating;
    private Long reviewCount;
    private String imageUrl;

    public static CafeDto of(Cafe cafe, String imageUrl) {
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
}
