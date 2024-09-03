package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.review.dto.ReviewDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewsResponse {

    private List<ReviewDto> reviews;

    public ReviewsResponse(final List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}
