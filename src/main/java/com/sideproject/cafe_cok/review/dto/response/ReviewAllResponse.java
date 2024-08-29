package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewAllResponse {

    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;

    public static ReviewAllResponse of(
            final List<KeywordCountDto> userChoiceKeywords, final List<CafeDetailReviewDto> reviews){

        return ReviewAllResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }
}
