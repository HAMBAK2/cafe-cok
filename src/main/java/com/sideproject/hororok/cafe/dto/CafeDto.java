package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class CafeDto {

    private final Long id;

    private final String name;

    private final String phoneNumber;

    private final String roadAddress;
    private final BigDecimal longitude; //경도
    private final BigDecimal latitude; //위도
    private final BigDecimal starRating;
    private final Long reviewCount;
    private final String image;

    public static CafeDto of(Cafe cafe, String imageUrl) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating())
                .reviewCount(cafe.getReviewCount())
                .image(imageUrl)
                .build();
    }
}
