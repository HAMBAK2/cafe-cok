package com.sideproject.hororok.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryAndKeyword {

    private final String category;
    private final List<String> keywords;

}
