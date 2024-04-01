package com.sideproject.hororok.cafe.cond;


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
    private final List<String> keywords;

    public static CafeCategorySearchCond of(BigDecimal latitude, BigDecimal longitude, List<String> keywords) {

        return CafeCategorySearchCond.builder()
                .latitude(latitude)
                .longitude(longitude)
                .keywords(keywords)
                .build();
    }
}
