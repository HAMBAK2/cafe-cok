package com.sideproject.cafe_cok.review.dto;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 상세 DTO")
public class ReviewDto {

    @Schema(description = "리뷰가 작성된 카페 ID", example = "1")
    private Long cafeId;

    @Schema(description = "리뷰가 작성된 카페 이름", example = "카페명")
    private String cafeName;

    @Schema(description = "리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "리뷰 별점", example = "4")
    private Integer starRating;

    @Schema(description = "리뷰 내용", example = "리뷰 내용")
    private String content;

    @Schema(description = "리뷰 특이사항", example = "리뷰 특이사항")
    private String specialNote;

    @Schema(description = "리뷰 생성일자", example = "2024-09-11")
    private LocalDate createdDate;

    @Schema(description = "리뷰 이미지 정보 리스트")
    private List<ImageUrlDto> imageUrls;

    @Schema(description = "리뷰 키워드 정보 리스트")
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
