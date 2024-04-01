package com.sideproject.hororok.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CategoryKeywordDto {
    private final List<CategoryAndKeyword> keywordsByCategory;
}
