package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ReviewInfo {

    private Long cafeId;
    private @Lob String content;
    private String specialNote;
    private CategoryKeywords categoryKeywords;
    private Integer starRating;
}
