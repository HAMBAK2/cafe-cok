package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.image.dto.ImageDto;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
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
    private LocalDate createdDate;
    private List<ImageDto> images;
    private List<KeywordDto> keywords;


    public static MyPageReviewDto of(
            final Review review, final List<ImageDto> images, final List<KeywordDto> keywords) {

        return MyPageReviewDto.builder()
                .cafeId(review.getCafe().getId())
                .cafeName(review.getCafe().getName())
                .reviewId(review.getId())
                .starRating(review.getStarRating())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .createdDate(review.getCreatedDate().toLocalDate())
                .images(images)
                .keywords(keywords)
                .build();
    }

}
