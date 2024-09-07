package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.cafe.dto.response.CafeSaveResponse;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Builder
public class CafeReviewsResponse extends RepresentationModel<CafeReviewsResponse> {

    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;

    public static CafeReviewsResponse of(
            final List<KeywordCountDto> userChoiceKeywords, final List<CafeDetailReviewDto> reviews){

        return CafeReviewsResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }
}
