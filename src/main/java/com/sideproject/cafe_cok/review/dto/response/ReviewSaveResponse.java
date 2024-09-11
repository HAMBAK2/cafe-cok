package com.sideproject.cafe_cok.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 저장 응답")
public class ReviewSaveResponse extends RepresentationModel<ReviewSaveResponse> {

    @Schema(description = "저장한 리뷰 ID", example = "1")
    private Long reviewId;

    @Schema(description = "저장한 리뷰의 카페 ID", example = "1")
    private Long cafeId;

    public ReviewSaveResponse(final Long reviewId,
                              final Long cafeId) {
        this.reviewId = reviewId;
        this.cafeId = cafeId;
    }
}
