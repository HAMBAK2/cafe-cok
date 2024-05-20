package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.domain.enums.OpenStatus;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
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
    private List<MenuImageUrlDto> menus;
    private List<ImageUrlDto> imageUrls;
    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;

    public static CafeDetailBasicInfoResponse of(
            final Cafe cafe, final OpenStatus openStatus, final List<String> businessHours,
            final List<String> closedDay, final List<MenuImageUrlDto> menus, final List<ImageUrlDto> imageUrls,
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
