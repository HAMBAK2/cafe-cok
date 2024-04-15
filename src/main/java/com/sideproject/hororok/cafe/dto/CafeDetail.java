package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.menu.dto.MenuInfo;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.cafe.domain.OpenStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CafeDetail {

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

    public CafeDetail(final Cafe cafe, final List<MenuInfo> menus, final OpenStatus openStatus,
                      final List<String> businessHours, final List<String> closedDay,
                      final List<String> reviewImageUrls, final List<ReviewDto> reviews,
                      final List<KeywordInfo> cafeKeywords, final List<String> cafeImageUrls) {

        this.id = cafe.getId();
        this.cafeName = cafe.getName();
        this.roadAddress = cafe.getRoadAddress();
        this.longitude = cafe.getLongitude();
        this.latitude = cafe.getLatitude();
        this.cafeImageUrls = cafeImageUrls;
        this.businessHours = businessHours;
        this.phoneNumber = cafe.getPhoneNumber();
        this.reviewCount = cafe.getReviewCount();
        this.menus = menus;
        this.starRating = cafe.getStarRating().doubleValue();
        this.closedDay = closedDay;
        this.openStatus = openStatus.getDescription();
        this.reviews = reviews;
        this.reviewImageUrls = reviewImageUrls;
        this.cafeKeywords = cafeKeywords;
    }
}
