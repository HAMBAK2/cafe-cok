package com.sideproject.cafe_cok.review.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class ReviewSaveResponse extends RepresentationModel<ReviewSaveResponse> {

    private Long reviewId;
    private Long cafeId;

    public ReviewSaveResponse(final Long reviewId,
                              final Long cafeId) {
        this.reviewId = reviewId;
        this.cafeId = cafeId;
    }
}
