package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CafeReSearchDto {

    private final boolean isExist;
    private final List<WithinRadiusCafeDto> cafes;
    private final CategoryKeywords categoryKeywords;

    public static CafeReSearchDto of(boolean isExist, List<WithinRadiusCafeDto> cafes, CategoryKeywords categoryKeywords) {
        return CafeReSearchDto.builder()
                .isExist(isExist)
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }

}
