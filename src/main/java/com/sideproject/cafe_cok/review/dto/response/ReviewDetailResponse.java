package com.sideproject.cafe_cok.review.dto.response;


import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewDetailResponse {

    private Long cafeId;
    private String cafeName;
    private Long reviewId;
    private Integer starRating;
    private String content;
    private String specialNote;
    private List<ImageDto> images;
    private CategoryKeywordsDto categoryKeywords;

    public static ReviewDetailResponse of(
            final Review review, final List<ImageDto> images, final CategoryKeywordsDto categoryKeywords) {

        return ReviewDetailResponse.builder()
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
