package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
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
    private final CategoryKeywordsDto categoryKeywordsDto;

    public static CafeReSearchDto of(boolean isExist, List<WithinRadiusCafeDto> cafes, CategoryKeywordsDto categoryKeywordsDto) {
        return CafeReSearchDto.builder()
                .isExist(isExist)
                .cafes(cafes)
                .categoryKeywordsDto(categoryKeywordsDto)
                .build();
    }

}
