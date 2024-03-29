package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryAndKeyword;
import com.sideproject.hororok.category.dto.CategoryKeywordDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CafeCategorySearchDto {

    private final List<Cafe> cafes;
    private final List<CategoryAndKeyword> keywordsByCategory;

    public static CafeReSearchDto of(List<Cafe> cafes, List<CategoryAndKeyword> keywordsByCategory) {
        return CafeReSearchDto.builder()
                .cafes(cafes)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }
}
