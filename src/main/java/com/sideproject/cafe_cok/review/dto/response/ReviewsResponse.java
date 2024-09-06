package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.review.dto.ReviewDto;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
public class ReviewsResponse extends RepresentationModel<ReviewsResponse> {

    private List<ReviewDto> reviews;

    public ReviewsResponse(final List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}
