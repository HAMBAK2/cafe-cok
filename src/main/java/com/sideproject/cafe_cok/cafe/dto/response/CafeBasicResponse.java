package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.domain.enums.OpenStatus;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.menu.dto.MenuImageDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 기본 정보 조회 응답")
public class CafeBasicResponse extends RepresentationModel<CafeBasicResponse> {

    @Schema(description = "카페 도로명 주소", example = "서울 종로구 종로5길 7")
    private String roadAddress;

    @Schema(description = "카페 전화번호", example = "000-0000-0000")
    private String phoneNumber;

    @Schema(description = "카페 영업중 여부", example = "OPEN")
    private String openStatus;

    @Schema(description = "카페 영업 시간 리스트", example = "수 12:00~21:00")
    private List<String> businessHours;

    @Schema(description = "카페 휴무일 리스트", example = "월")
    private List<String> closedDay;

    @Schema(description = "카페 메뉴 리스트")
    private List<MenuImageDto> menus;

    @Schema(description = "카페 이미지 리스트")
    private List<ImageUrlDto> imageUrls;

    @Schema(description = "카페에 대해 사용자가 선택한 키워드와 횟수 리스트")
    private List<KeywordCountDto> userChoiceKeywords;

    @Schema(description = "카페 리뷰 리스트")
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
