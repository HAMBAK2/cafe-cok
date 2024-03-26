package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.Menu.dto.MenuDto;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryKeywordDto;
import com.sideproject.hororok.image.dto.ImageDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.utils.calculator.BusinessHoursUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@RequiredArgsConstructor
public class CafeBarSearchDto {

    private final boolean isExist;
    private final List<Cafe> cafe;
    private final CategoryKeywordDto keywordsByCategory;
    private final CafeDetailDto cafeDetail;

    //존재할 경우
    private final Long id;
    private final String cafeName;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final List<String> BusinessHours;
    private final List<String> closedDay;
    private final List<String> cafeImageUrls;
    private final String openStatus;
    private final String phoneNumber;

    private final List<MenuDto> menus;
    private final List<ReviewDto> reviews;
    private final List<ImageDto> images;
    private final List<KeywordDto> cafeKeywords;

    public static CafeBarSearchDto of(boolean isExist, List<Cafe> cafe, CategoryKeywordDto keywordsByCategory) {


        return CafeBarSearchDto.builder()
                .isExist(isExist)
                .cafe(cafe)
                .keywordsByCategory(keywordsByCategory)
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
                .cafe(cafeReSearchDto.getCafes())
                .keywordsByCategory(cafeReSearchDto.getKeywordsByCategory())
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
                .BusinessHours(cafeDetailDto.getBusinessHours())
                .closedDay(cafeDetailDto.getClosedDay())
                .cafeImageUrls(cafeDetailDto.getCafeImageUrls())
                .openStatus(cafeDetailDto.getOpenStatus())
                .phoneNumber(cafeDetailDto.getPhoneNumber())
                .menus(cafeDetailDto.getMenus())
                .reviews(cafeDetailDto.getReviews())
                .images(cafeDetailDto.getImages())
                .cafeKeywords(cafeDetailDto.getCafeKeywords())
                .build();
    }

}
