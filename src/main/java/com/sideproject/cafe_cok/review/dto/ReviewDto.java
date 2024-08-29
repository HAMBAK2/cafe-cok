package com.sideproject.cafe_cok.review.dto;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.review.domain.Review;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ReviewDto {

    private Long cafeId;
    private String cafeName;
    private Long reviewId;
    private Integer starRating;
    private String content;
    private String specialNote;
    private LocalDate createdDate;
    private List<ImageUrlDto> imageUrls;
    private List<KeywordDto> keywords;


    public ReviewDto(final Review review,
                     final List<ImageUrlDto> images,
                     final List<KeywordDto> keywords) {
        Cafe cafe = review.getCafe();
        this.cafeId = cafe.getId();
        this.cafeName = cafe.getName();
        this.reviewId = review.getId();
        this.starRating = review.getStarRating();
        this.content = review.getContent();
        this.specialNote = review.getSpecialNote();
        this.createdDate = review.getCreatedDate().toLocalDate();
        this.imageUrls = images;
        this.keywords = keywords;
    }
}
