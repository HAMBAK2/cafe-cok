package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDetail;
import com.sideproject.hororok.cafe.dto.WithinRadiusCafe;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordCount;
import com.sideproject.hororok.menu.dto.MenuInfo;
import com.sideproject.hororok.review.dto.ReviewDetail;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CafeFindBarResponse {

    private final boolean isExist;

    //검색한 카페가 존재하지 않는 경우
    private final CategoryKeywordsDto categoryKeywords;
    private final List<WithinRadiusCafe> cafes;

    //검색한 카페가 존재하는 경우
    private final Long id;
    private final Long reviewCount;
    private final String cafeName;
    private final String openStatus;
    private final String phoneNumber;
    private final String roadAddress;
    private final BigDecimal longitude;
    private final BigDecimal latitude;

    private final List<String> businessHours;
    private final List<String> closedDay;
    private final List<String> cafeImageUrls;
    private final List<String> reviewImageUrls;
    private final List<MenuInfo> menus;
    private final List<ReviewDetail> reviews;
    private final List<KeywordCount> cafeKeywords;

    public static CafeFindBarResponse notExistOf(List<WithinRadiusCafe> cafes,
                                                 CategoryKeywordsDto categoryKeywords) {

        return CafeFindBarResponse.builder()
                .isExist(false)
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }

    public static CafeFindBarResponse existFrom(CafeDetail cafeDetail) {

        return CafeFindBarResponse.builder()
                .isExist(true)
                .id(cafeDetail.getId())
                .cafeName(cafeDetail.getCafeName())
                .roadAddress(cafeDetail.getRoadAddress())
                .longitude(cafeDetail.getLongitude())
                .latitude(cafeDetail.getLatitude())
                .businessHours(cafeDetail.getBusinessHours())
                .closedDay(cafeDetail.getClosedDay())
                .cafeImageUrls(cafeDetail.getCafeImageUrls())
                .openStatus(cafeDetail.getOpenStatus())
                .phoneNumber(cafeDetail.getPhoneNumber())
                .menus(cafeDetail.getMenus())
                .reviews(cafeDetail.getReviews())
                .reviewImageUrls(cafeDetail.getReviewImageUrls())
                .cafeKeywords(cafeDetail.getCafeKeywords())
                .reviewCount(cafeDetail.getReviewCount())
                .build();
    }

}
