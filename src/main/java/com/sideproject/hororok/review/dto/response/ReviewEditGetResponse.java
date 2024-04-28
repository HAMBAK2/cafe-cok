package com.sideproject.hororok.review.dto.response;


import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.dto.ReviewImageDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewEditGetResponse {

    private Long cafeId;
    private String cafeName;
    private Long reviewId;
    private Integer starRating;
    private String content;
    private String specialNote;
    private List<ReviewImageDto> images;
    private CategoryKeywordsDto categoryKeywords;


    public static ReviewEditGetResponse of(
            final Review review, final List<ReviewImageDto> images, final CategoryKeywordsDto categoryKeywords) {

        return ReviewEditGetResponse.builder()
                .cafeId(review.getCafe().getId())
                .cafeName(review.getCafe().getName())
                .reviewId(review.getId())
                .starRating(review.getStarRating())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(images)
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
