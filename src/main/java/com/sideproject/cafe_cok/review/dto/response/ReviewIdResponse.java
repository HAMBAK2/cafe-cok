package com.sideproject.cafe_cok.review.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
public class ReviewIdResponse extends RepresentationModel<ReviewIdResponse> {

    private Long reviewId;

    public ReviewIdResponse(final Long reviewId) {
        this.reviewId = reviewId;
    }
}
