package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryKeywordDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.operationHours.dto.BusinessScheduleDto;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.utils.enums.OpenStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CafeDetailDto {

    private final boolean isExist;

    //존재하지 않을 경우
    private final List<Cafe> cafes;
    private final CategoryKeywordDto keywordsByCategory;

    //존재할 경우
    private final Long id;
    private final String cafeName;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final List<String> cafeImageUrls;
    private final String phoneNumber;
    private final Long reviewCount;
    private final BusinessScheduleDto businessSchedules;

    private final List<MenuDto> menus;
    private final List<ReviewDto> reviews;
    private final List<String> reviewImageUrls;
    private final List<KeywordDto> cafeKeywords;

    public static CafeDetailDto of(Cafe cafe, List<MenuDto> menus, List<String> reviewImageUrls
            , List<ReviewDto> reviews, List<KeywordDto> cafeKeywords, List<String> cafeImageUrls,
                                   BusinessScheduleDto businessSchedule) {


        /**
         * 여기 날짜 정보 변경된 걸로 받을 수 있게 해야 함
         */
        return CafeDetailDto.builder()
                .isExist(true)
                .id(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .longitude(cafe.getLongitude())
                .latitude(cafe.getLatitude())
                .cafeImageUrls(cafeImageUrls)
                .phoneNumber(cafe.getPhoneNumber())
                .reviewCount(cafe.getReviewCount())
                .menus(menus)
                .reviews(reviews)
                .reviewImageUrls(reviewImageUrls)
                .cafeKeywords(cafeKeywords)
                .businessSchedules(businessSchedule)
                .build();
    }

    public static CafeDetailDto from(CafeReSearchDto cafeReSearchDto) {

        return CafeDetailDto.builder()
                .isExist(false)
                .cafes(cafeReSearchDto.getCafes())
                .keywordsByCategory(cafeReSearchDto.getKeywordsByCategory())
                .build();
    }

}
