package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewPageResponse {

    private List<KeywordCountDto> userChoiceKeywords;
    private List<CafeDetailReviewDto> reviews;
    private Long cursor;
    private Boolean hasNextPage;

    public static ReviewPageResponse of(final List<KeywordCountDto> userChoiceKeywords,
                                        final List<CafeDetailReviewDto> reviews){

        return ReviewPageResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .build();
    }

    public static ReviewPageResponse of(final List<KeywordCountDto> userChoiceKeywords,
                                        final List<CafeDetailReviewDto> reviews,
                                        final Long cursor,
                                        final Boolean hasNextPage) {

        return ReviewPageResponse.builder()
                .userChoiceKeywords(userChoiceKeywords)
                .reviews(reviews)
                .hasNextPage(hasNextPage)
                .cursor(cursor)
                .build();
    }
}
