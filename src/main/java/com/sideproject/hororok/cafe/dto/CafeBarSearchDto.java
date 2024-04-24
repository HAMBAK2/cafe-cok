package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.dto.ReviewDetailDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CafeBarSearchDto {

    private final boolean isExist;
    private final List<WithinRadiusCafeDto> cafes;
    private final CategoryKeywordsDto categoryKeywords;
    private final CafeDetailDto cafeDetail;

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

    private final List<MenuDto> menus;
    private final List<ReviewDetailDto> reviews;
    private final List<String> reviewImageUrls;
    private final List<KeywordDto> cafeKeywords;

    public static CafeBarSearchDto of(boolean isExist, List<WithinRadiusCafeDto> cafe, CategoryKeywordsDto categoryKeywords) {


        return CafeBarSearchDto.builder()
                .isExist(isExist)
                .cafes(cafe)
                .categoryKeywords(categoryKeywords)
                .build();
    }

    public static CafeBarSearchDto of(boolean isExist, CafeDetailDto cafeDetail) {


        return CafeBarSearchDto.builder()
                .isExist(isExist)
                .cafeDetail(cafeDetail)
                .build();
    }


    public static CafeBarSearchDto from(CafeReSearchDto cafeReSearchDto) {

        return CafeBarSearchDto.builder()
                .isExist(false)
                .cafes(cafeReSearchDto.getCafes())
                .categoryKeywords(cafeReSearchDto.getCategoryKeywords())
                .build();
    }

    public static CafeBarSearchDto from(CafeDetailDto cafeDetailDto) {

        return CafeBarSearchDto.builder()
                .isExist(true)
                .id(cafeDetailDto.getId())
                .cafeName(cafeDetailDto.getCafeName())
                .roadAddress(cafeDetailDto.getRoadAddress())
                .longitude(cafeDetailDto.getLongitude())
                .latitude(cafeDetailDto.getLatitude())
                .businessHours(cafeDetailDto.getBusinessHours())
                .closedDay(cafeDetailDto.getClosedDay())
                .cafeImageUrls(cafeDetailDto.getCafeImageUrls())
                .openStatus(cafeDetailDto.getOpenStatus())
                .phoneNumber(cafeDetailDto.getPhoneNumber())
                .menus(cafeDetailDto.getMenus())
                .reviews(cafeDetailDto.getReviews())
                .reviewImageUrls(cafeDetailDto.getReviewImageUrls())
                .cafeKeywords(cafeDetailDto.getCafeKeywords())
                .reviewCount(cafeDetailDto.getReviewCount())
                .build();
    }

}
