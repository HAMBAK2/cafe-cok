package com.sideproject.hororok.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewEditPatchResponse {

    private Long reviewId;


    protected ReviewEditPatchResponse() {
    }

    public ReviewEditPatchResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
