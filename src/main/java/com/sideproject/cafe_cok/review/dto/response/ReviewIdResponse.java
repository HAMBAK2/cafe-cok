package com.sideproject.cafe_cok.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 수정/삭제 응답")
public class ReviewIdResponse extends RepresentationModel<ReviewIdResponse> {

    @Schema(description = "삭제/수정한 리뷰 ID", example = "1")
    private Long reviewId;

    public ReviewIdResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
