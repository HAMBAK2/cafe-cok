package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.review.dto.MyPageReviewDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MyPageReviewResponse {

    List<MyPageReviewDto> reviews;

    protected MyPageReviewResponse() {
    }

    public MyPageReviewResponse(final List<MyPageReviewDto> reviews) {
        this.reviews = reviews;
    }
}
