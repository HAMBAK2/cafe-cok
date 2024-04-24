package com.sideproject.hororok.cafe.dto.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CafeFindCategoryRequest {

    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> keywords;

    public CafeFindCategoryRequest(final BigDecimal latitude,
                                   final BigDecimal longitude,
                                   final List<String> keywords) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.keywords = keywords;
    }
}
