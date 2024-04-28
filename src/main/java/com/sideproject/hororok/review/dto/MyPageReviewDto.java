package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageReviewDto {

    private Long cafeId;
    private String cafeName;
    private Long reviewId;
    private Integer starRating;
    private String content;
    private String specialNote;
    private List<ReviewImageDto> images;
    private List<KeywordDto> keywords;


    public static MyPageReviewDto of(
            final Review review, final List<ReviewImageDto> images, final List<KeywordDto> keywords) {

        return MyPageReviewDto.builder()
                .cafeId(review.getCafe().getId())
                .cafeName(review.getCafe().getName())
                .reviewId(review.getId())
                .starRating(review.getStarRating())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(images)
                .keywords(keywords)
                .build();
    }



}
