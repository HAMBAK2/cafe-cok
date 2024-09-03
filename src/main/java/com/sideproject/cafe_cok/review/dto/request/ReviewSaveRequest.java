package com.sideproject.cafe_cok.review.dto.request;


import lombok.Getter;

import java.util.List;

@Getter
public class ReviewSaveRequest {

    private Long cafeId;
    private String content;
    private String specialNote;
    private List<String> keywords;
    private Integer starRating;

    public ReviewSaveRequest(
            final Long cafeId, final String content, final String specialNote,
            final List<String> keywords, final Integer starRating) {
        this.cafeId = cafeId;
        this.content = content;
        this.specialNote = specialNote;
        this.keywords = keywords;
        this.starRating = starRating;
    }
}
