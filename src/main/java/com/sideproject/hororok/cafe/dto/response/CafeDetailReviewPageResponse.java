package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.keword.dto.KeywordCountDto;
import com.sideproject.hororok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailReviewPageResponse {

    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;
    private Boolean hasNextPage;
    private Long cursor;

    public static CafeDetailReviewPageResponse of(
            final List<KeywordCountDto> userChoiceKeywords, final List<CafeDetailReviewDto> reviews){

        return CafeDetailReviewPageResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }

    public static CafeDetailReviewPageResponse of(
            final List<KeywordCountDto> userChoiceKeywords, final List<CafeDetailReviewDto> reviews,
            final Long cursor, final Boolean hasNextPage) {

        return CafeDetailReviewPageResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .hasNextPage(hasNextPage)
                .cursor(cursor)
                .build();
    }
}
