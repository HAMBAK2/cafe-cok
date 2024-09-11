package com.sideproject.cafe_cok.review.dto.response;

import com.sideproject.cafe_cok.review.dto.ReviewDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 목록 응답")
public class ReviewsResponse extends RepresentationModel<ReviewsResponse> {

    @Schema(description = "조회한 리뷰 상세 리스트")
    private List<ReviewDto> reviews;

    public ReviewsResponse(final List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}
