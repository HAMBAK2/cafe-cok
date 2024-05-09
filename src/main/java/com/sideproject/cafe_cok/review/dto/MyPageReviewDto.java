package com.sideproject.cafe_cok.review.dto;

import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.review.domain.Review;
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
