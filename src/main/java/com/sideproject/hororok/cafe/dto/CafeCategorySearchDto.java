package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CafeCategorySearchDto {

    private final List<WithinRadiusCafeDto> cafes;
    private final CategoryKeywords categoryKeywords;

    public static CafeReSearchDto of(List<WithinRadiusCafeDto> cafes, CategoryKeywords categoryKeywords) {
        return CafeReSearchDto.builder()
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
