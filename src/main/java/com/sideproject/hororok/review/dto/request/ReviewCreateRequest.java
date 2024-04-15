package com.sideproject.hororok.review.dto.request;


import com.sideproject.hororok.category.dto.CategoryKeywords;
import jakarta.persistence.Lob;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    private final Long cafeId;
    private final @Lob String content;
    private final String specialNote;
    private final CategoryKeywords categoryKeywords;
    private final Integer starRating;

    public ReviewCreateRequest(
            final Long cafeId, final String content, final String specialNote,
            final CategoryKeywords categoryKeywords, final Integer starRating) {
        this.cafeId = cafeId;
        this.content = content;
        this.specialNote = specialNote;
        this.categoryKeywords = categoryKeywords;
        this.starRating = starRating;
    }
}
