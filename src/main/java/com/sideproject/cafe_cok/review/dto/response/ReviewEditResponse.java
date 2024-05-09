package com.sideproject.cafe_cok.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewEditResponse {

    private Long reviewId;


    protected ReviewEditResponse() {
    }

    public ReviewEditResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
