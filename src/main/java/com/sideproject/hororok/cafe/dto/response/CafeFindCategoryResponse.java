package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeFindCategoryResponse {

    private final List<CafeDto> cafes;
    private final CategoryKeywordsDto categoryKeywords;

    public static CafeFindCategoryResponse of(final List<CafeDto> cafes,
                                              final CategoryKeywordsDto categoryKeywords) {
        return CafeFindCategoryResponse.builder()
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
