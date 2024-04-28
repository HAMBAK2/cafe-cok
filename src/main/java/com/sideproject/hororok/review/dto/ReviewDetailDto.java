package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReviewDetailDto {

    private final Long id;
    private final String content;
    private final String specialNote;
    private final List<ReviewImageDto> images;
    private final List<KeywordDto> keywords;
    private final Integer starRating;
    private final LocalDate createdDate;

    private final String nickname;


    public static ReviewDetailDto of(final Review review,
                                     final List<ReviewImageDto> images,
                                     final List<KeywordDto> keywords) {

        return ReviewDetailDto.builder()
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
