package com.sideproject.hororok.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CategoryKeywordDto {
    private final List<CategoryKeywords> keywordsByCategory;
}
