package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReviewDetail {

    private final Long id;
    private final String content;
    private final String specialNote;
    private final List<ReviewImage> images;
    private final List<KeywordInfo> keywords;
    private final Integer starRating;
    private final LocalDate createdDate;

    private final String nickname;


    public static ReviewDetail of(Review review, List<KeywordInfo> keywords, String nickname) {

        return ReviewDetail.builder()
                .id(review.getId())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(review.getImages())
                .starRating(review.getStarRating())
                .createdDate(LocalDate.from(review.getCreatedDate()))
                .keywords(keywords)
                .nickname(nickname)
                .build();


    }
}
