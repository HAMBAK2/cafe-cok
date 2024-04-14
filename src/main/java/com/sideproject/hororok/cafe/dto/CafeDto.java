package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

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

    public static CafeDto from(Cafe cafe) {
        return CafeDto.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .phoneNumber(cafe.getPhoneNumber())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .starRating(cafe.getStarRating())
                .reviewCount(cafe.getReviewCount())
                .image(cafe.getImages().get(0).getImageUrl())
                .build();
    }

}
