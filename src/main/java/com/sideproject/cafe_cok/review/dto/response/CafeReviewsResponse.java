package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Builder
@Schema(description = "카페 리뷰 조회 응답")
public class CafeReviewsResponse extends RepresentationModel<CafeReviewsResponse> {

    @Schema(description = "카페에 대해 사용자가 선택한 키워드와 횟수 리스트")
    private List<KeywordCountDto> userChoiceKeywords;

    @Schema(description = "카페 리뷰 리스트")
    private List<CafeDetailReviewDto> reviews;

    public static CafeReviewsResponse of(final List<KeywordCountDto> userChoiceKeywords,
                                         final List<CafeDetailReviewDto> reviews){

        return CafeReviewsResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }
}
