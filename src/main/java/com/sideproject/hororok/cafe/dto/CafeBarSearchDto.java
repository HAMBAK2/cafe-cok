package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import com.sideproject.hororok.menu.dto.MenuInfo;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.review.dto.ReviewDetail;
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
    private final CategoryKeywords categoryKeywords;
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

    private final List<MenuInfo> menus;
    private final List<ReviewDetail> reviews;
    private final List<String> reviewImageUrls;
    private final List<KeywordInfo> cafeKeywords;

    public static CafeBarSearchDto of(boolean isExist, List<WithinRadiusCafeDto> cafe, CategoryKeywords categoryKeywords) {


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
