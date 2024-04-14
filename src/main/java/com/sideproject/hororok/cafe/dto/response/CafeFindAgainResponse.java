package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.WithinRadiusCafe;
import com.sideproject.hororok.cafe.dto.WithinRadiusCafeDto;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CafeFindAgainResponse {

    private final boolean isExist;
    private final List<WithinRadiusCafe> cafes;
    private final CategoryKeywords categoryKeywords;

    public static CafeFindAgainResponse of(List<WithinRadiusCafe> cafes, CategoryKeywords categoryKeywords) {
        return CafeFindAgainResponse.builder()
                .isExist(!cafes.isEmpty())
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
