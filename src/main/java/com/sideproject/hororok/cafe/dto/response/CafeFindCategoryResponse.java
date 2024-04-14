package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.WithinRadiusCafe;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeFindCategoryResponse {

    private final List<WithinRadiusCafe> cafes;
    private final CategoryKeywords categoryKeywords;

    public static CafeFindCategoryResponse of(final List<WithinRadiusCafe> cafes,
                                              final CategoryKeywords categoryKeywords) {
        return CafeFindCategoryResponse.builder()
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
