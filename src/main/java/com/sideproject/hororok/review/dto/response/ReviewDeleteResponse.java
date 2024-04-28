package com.sideproject.hororok.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewDeleteResponse {

    private Long reviewId;

    protected ReviewDeleteResponse() {}

    public ReviewDeleteResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
