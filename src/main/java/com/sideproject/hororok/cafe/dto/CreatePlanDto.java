package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryKeywordDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CreatePlanDto {

    private final boolean isExist;
    private final List<Cafe> cafe;
    private final CategoryKeywordDto keywordsByCategory;

    public static CreatePlanDto of(boolean isExist, List<Cafe> cafe, CategoryKeywordDto keywordsByCategory) {

        return CreatePlanDto.builder()
                .isExist(isExist)
                .cafe(cafe)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }

    public static CreatePlanDto of(boolean isExist, CategoryKeywordDto keywordsByCategory) {

        return CreatePlanDto.builder()
                .isExist(isExist)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }
}
