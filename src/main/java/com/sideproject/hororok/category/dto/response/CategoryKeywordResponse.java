package com.sideproject.hororok.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CategoryKeywordResponse {
    private final Map<String, List<String>> keywordsByCategory;
}
