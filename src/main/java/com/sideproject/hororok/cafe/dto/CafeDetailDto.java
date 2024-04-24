package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.dto.ReviewDetailDto;
import com.sideproject.hororok.cafe.domain.enums.OpenStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CafeDetailDto {

    private final boolean isExist;

    //존재하지 않을 경우
    private final List<WithinRadiusCafeDto> cafes;
    private final CategoryKeywordsDto categoryKeywordsDto;

    //존재할 경우
    private final Long id;
    private final String cafeName;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final List<String> businessHours;
    private final List<String> closedDay;
    private final List<String> cafeImageUrls;
    private final String openStatus;
    private final String phoneNumber;
    private final Long reviewCount;
    private final double starRating;

    private final List<MenuDto> menus;
    private final List<ReviewDetailDto> reviews;
    private final List<String> reviewImageUrls;
    private final List<KeywordDto> cafeKeywords;

    public static CafeDetailDto of(Cafe cafe, List<MenuDto> menus, OpenStatus openStatus, List<String> businessHours, List<String> closedDay,
                                   List<String> reviewImageUrls, List<ReviewDetailDto> reviews,
                                   List<KeywordDto> cafeKeywords, List<String> cafeImageUrls) {


        return CafeDetailDto.builder()
                .isExist(true)
                .id(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .cafeImageUrls(cafeImageUrls)
                .businessHours(businessHours)
                .phoneNumber(cafe.getPhoneNumber())
                .reviewCount(cafe.getReviewCount())
                .menus(menus)
                .starRating(cafe.getStarRating().doubleValue())
                .closedDay(closedDay)
                .openStatus(openStatus.getDescription())
                .reviews(reviews)
                .reviewImageUrls(reviewImageUrls)
                .cafeKeywords(cafeKeywords)
                .build();
    }

    public static CafeDetailDto from(CafeReSearchDto cafeReSearchDto) {

        return CafeDetailDto.builder()
                .isExist(false)
                .cafes(cafeReSearchDto.getCafes())
                .categoryKeywordsDto(cafeReSearchDto.getCategoryKeywords())
                .build();
    }

}