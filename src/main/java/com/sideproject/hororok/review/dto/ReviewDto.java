package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.review.Entity.Review;
import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewDto {

    private final Long id;
    private final String content;
    private final String specialNote;
    private final List<ReviewImage> images;
    private final List<Keyword> keywords;

    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(review.getImages())
                .keywords(review.getKeywords())
                .build();
    }

}
