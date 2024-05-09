package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailReviewPageResponse {

    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;
    private Long cursor;
    private Boolean hasNextPage;

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
