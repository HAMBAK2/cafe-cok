package com.sideproject.cafe_cok.review.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEditResponse {

    private Long reviewId;

    public ReviewEditResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
