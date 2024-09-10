package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.domain.enums.OpenStatus;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.menu.dto.MenuImageDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
public class CafeBasicResponse extends RepresentationModel<CafeBasicResponse> {

    private String roadAddress;
    private String phoneNumber;
    private String openStatus;
    private List<String> businessHours;
    private List<String> closedDay;
    private List<MenuImageDto> menus;
    private List<ImageUrlDto> imageUrls;
    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;

    @Builder
    public CafeBasicResponse(final String roadAddress,
                             final String phoneNumber,
                             final OpenStatus openStatus,
                             final List<String> businessHours,
                             final List<String> closedDay,
                             final List<MenuImageDto> menus,
                             final List<ImageUrlDto> imageUrls,
                             final List<KeywordCountDto> userChoiceKeywords,
                             final List<CafeDetailReviewDto> reviews) {
        this.roadAddress = roadAddress;
        this.phoneNumber = phoneNumber;
        this.openStatus = openStatus.getValue();
        this.businessHours = businessHours;
        this.closedDay = closedDay;
        this.menus = menus;
        this.imageUrls = imageUrls;
        this.userChoiceKeywords = userChoiceKeywords;
        this.reviews = reviews;

    }
}
