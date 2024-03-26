package com.sideproject.hororok.cafe.cond;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
public class CafeSearchCond {

    private final BigDecimal longitude;
    private final BigDecimal latitude;

    public static CafeSearchCond from(CafeCategorySearchCond cafeCategorySearchCond) {
        return CafeSearchCond.builder()
                .longitude(cafeCategorySearchCond.getLongitude())
                .latitude(cafeCategorySearchCond.getLatitude())
                .build();
    }


}
