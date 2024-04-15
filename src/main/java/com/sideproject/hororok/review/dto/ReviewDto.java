package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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
    private final Integer starRating;
    private final LocalDate createdDate;

    private final String userNickname;


    public static ReviewDto of(Review review, String userNickname) {

        return ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(review.getImages())
                .starRating(review.getStarRating())
                .createdDate(LocalDate.from(review.getCreatedDate()))
                .keywords(review.getKeywords())
                .userNickname(userNickname)
                .build();


    }
}
