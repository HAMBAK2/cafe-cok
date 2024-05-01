package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.keword.dto.KeywordCountDto;
import com.sideproject.hororok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailReviewAllResponse {

    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;

    public static CafeDetailReviewAllResponse of(
            final List<KeywordCountDto> userChoiceKeywords, final List<CafeDetailReviewDto> reviews){

        return CafeDetailReviewAllResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }
}
