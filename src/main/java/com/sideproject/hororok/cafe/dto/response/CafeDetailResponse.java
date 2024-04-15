package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDetail;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.menu.dto.MenuInfo;
import com.sideproject.hororok.review.dto.ReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class CafeDetailResponse {

    private final Long id;
    private final Long reviewCount;
    private final String cafeName;
    private final String roadAddress;
    private final String openStatus;
    private final String phoneNumber;
    private final double starRating;
    private final BigDecimal longitude;
    private final BigDecimal latitude;

    private final List<String> businessHours;
    private final List<String> closedDay;
    private final List<String> cafeImageUrls;
    private final List<MenuInfo> menus;
    private final List<String> reviewImageUrls;
    private final List<ReviewDto> reviews;
    private final List<KeywordInfo> cafeKeywords;

    public static CafeDetailResponse from(final CafeDetail cafeDetail) {

        return CafeDetailResponse.builder()
                .id(cafeDetail.getId())
                .cafeName(cafeDetail.getCafeName())
                .roadAddress(cafeDetail.getRoadAddress())
                .longitude(cafeDetail.getLongitude())
                .latitude(cafeDetail.getLatitude())
                .cafeImageUrls(cafeDetail.getCafeImageUrls())
                .businessHours(cafeDetail.getBusinessHours())
                .phoneNumber(cafeDetail.getPhoneNumber())
                .reviewCount(cafeDetail.getReviewCount())
                .menus(cafeDetail.getMenus())
                .starRating(cafeDetail.getStarRating())
                .closedDay(cafeDetail.getClosedDay())
                .openStatus(cafeDetail.getOpenStatus())
                .reviews(cafeDetail.getReviews())
                .reviewImageUrls(cafeDetail.getReviewImageUrls())
                .cafeKeywords(cafeDetail.getCafeKeywords())
                .build();
    }
}
