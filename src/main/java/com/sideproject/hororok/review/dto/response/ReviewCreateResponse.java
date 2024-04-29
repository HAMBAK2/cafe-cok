package com.sideproject.hororok.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewCreateResponse {

    private Long reviewId;

    protected ReviewCreateResponse() {
    }

    public ReviewCreateResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
