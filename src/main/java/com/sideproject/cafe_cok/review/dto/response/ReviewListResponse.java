package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.review.dto.ReviewDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewListResponse {

    private List<ReviewDto> reviews;

    public ReviewListResponse(final List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}
