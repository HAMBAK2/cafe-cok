package com.sideproject.hororok.cafe.cond;


import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class CafeCategorySearchCond {

    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final CategoryKeywords categoryKeywords;

    public static CafeCategorySearchCond of(BigDecimal latitude, BigDecimal longitude, CategoryKeywords categoryKeywords) {

        return CafeCategorySearchCond.builder()
                .latitude(latitude)
                .longitude(longitude)
                .categoryKeywords(categoryKeywords)
                .build();
    }
}
