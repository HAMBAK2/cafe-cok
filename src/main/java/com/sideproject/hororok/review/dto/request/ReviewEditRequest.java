package com.sideproject.hororok.review.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewEditRequest {

    private String content;
    private String specialNote;
    private Integer starRating;
    private List<String> keywords;
    private List<Long> deletedImageIds;

    public ReviewEditRequest(
            final String content, final String specialNote, final List<String> keywords,
            final Integer starRating, final List<Long> deleteImageIds) {
        this.content = content;
        this.specialNote = specialNote;
        this.keywords = keywords;
        this.starRating = starRating;
        this.deletedImageIds = deleteImageIds;
    }
}
