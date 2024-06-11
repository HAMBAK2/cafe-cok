package com.sideproject.cafe_cok.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewCreateResponse {

    private Long reviewId;
    private Long cafeId;

    public ReviewCreateResponse(final Long reviewId,
                                final Long cafeId) {
        this.reviewId = reviewId;
        this.cafeId = cafeId;
    }
}
