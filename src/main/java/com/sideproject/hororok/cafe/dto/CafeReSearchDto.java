package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
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
    private final CategoryKeywordDto keywordsByCategory;

    public static CafeReSearchDto of(boolean isExist, List<Cafe> cafes, CategoryKeywordDto keywordsByCategory) {
        return CafeReSearchDto.builder()
                .isExist(isExist)
                .cafes(cafes)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }

}
