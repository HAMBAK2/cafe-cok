package com.sideproject.cafe_cok.review.dto.response;


import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Builder
@Schema(description = "리뷰 조회 응답")
public class ReviewResponse extends RepresentationModel<ReviewResponse> {

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

    @Schema(description = "리뷰 이미지 정보 리스트")
    private List<ImageDto> images;

    @Schema(description = "리뷰 카테고리 별 키워드")
    private CategoryKeywordsDto categoryKeywords;

    public static ReviewResponse of(final Review review,
                                    final List<Image> images,
                                    final CategoryKeywordsDto categoryKeywords) {

        return ReviewResponse.builder()
                .cafeId(review.getCafe().getId())
                .cafeName(review.getCafe().getName())
                .reviewId(review.getId())
                .starRating(review.getStarRating())
                .content(review.getContent())
                .specialNote(review.getSpecialNote())
                .images(ImageDto.fromList(images))
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
