package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.enums.OpenStatus;
import com.sideproject.hororok.keword.dto.KeywordCountDto;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailBasicInfoResponse {

    private String roadAddress;
    private String openStatus;
    private List<String> businessHours;
    private List<String> closedDay;
    private String phoneNumber;
    private List<MenuDto> menus;
    private List<String> imageUrls;
    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;

    public static CafeDetailBasicInfoResponse of(
            final Cafe cafe, final OpenStatus openStatus, final List<String> businessHours,
            final List<String> closedDay, final List<MenuDto> menus, final List<String> imageUrls,
            final List<KeywordCountDto> userChoiceKeywords, final List<CafeDetailReviewDto> reviews) {
        return CafeDetailBasicInfoResponse.builder()
                .roadAddress(cafe.getRoadAddress())
                .openStatus(openStatus.getValue())
                .businessHours(businessHours)
                .closedDay(closedDay)
                .phoneNumber(cafe.getPhoneNumber())
                .menus(menus)
                .imageUrls(imageUrls)
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }

}
