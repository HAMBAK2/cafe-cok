package com.sideproject.hororok.cafe.cond;


import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
public class CafeCategorySearchCond {

    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final CategoryKeywordsDto categoryKeywordsDto;

    public static CafeCategorySearchCond of(BigDecimal latitude, BigDecimal longitude, CategoryKeywordsDto categoryKeywordsDto) {

        return CafeCategorySearchCond.builder()
                .latitude(latitude)
                .longitude(longitude)
                .categoryKeywordsDto(categoryKeywordsDto)
                .build();
    }
}
