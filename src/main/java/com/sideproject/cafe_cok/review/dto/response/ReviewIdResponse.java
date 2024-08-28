package com.sideproject.cafe_cok.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewIdResponse {

    private Long reviewId;

    protected ReviewIdResponse() {}

    public ReviewIdResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
