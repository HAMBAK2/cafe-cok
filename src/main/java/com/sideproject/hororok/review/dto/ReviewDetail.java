package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.review.domain.Review;
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
    private final List<ReviewImageInfo> images;
    private final List<KeywordInfo> keywords;
    private final Integer starRating;
    private final LocalDate createdDate;

    private final String nickname;


    public static ReviewDetail of(final Review review,
                                  final List<ReviewImageInfo> images,
                                  final List<KeywordInfo> keywords) {

        return ReviewDetail.builder()
                .id(review.getId())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(images)
                .starRating(review.getStarRating())
                .createdDate(LocalDate.from(review.getCreatedDate()))
                .keywords(keywords)
                .nickname(review.getMember().getNickname())
                .build();


    }
}
