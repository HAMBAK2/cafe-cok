package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class SaveReviewDto {

    private Long cafeId;
    private @Lob String content;
    private String specialNote;
    private CategoryKeywords categoryKeywords;
    private Integer starRating;
}
