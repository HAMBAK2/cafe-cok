package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryAndKeyword;
import com.sideproject.hororok.category.dto.CategoryKeywordDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Builder
@Getter
@RequiredArgsConstructor
public class CafeReSearchDto {

    private final boolean isExist;
    private final List<Cafe> cafes;
    private final List<CategoryAndKeyword> keywordsByCategory;

    public static CafeReSearchDto of(boolean isExist, List<Cafe> cafes, List<CategoryAndKeyword> keywordsByCategory) {
        return CafeReSearchDto.builder()
                .isExist(isExist)
                .cafes(cafes)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }

}
