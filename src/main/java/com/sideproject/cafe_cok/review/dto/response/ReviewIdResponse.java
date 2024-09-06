package com.sideproject.cafe_cok.review.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class ReviewIdResponse extends RepresentationModel<ReviewIdResponse> {

    private Long reviewId;

    protected ReviewIdResponse() {}

    public ReviewIdResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
