package com.sideproject.hororok.cafe.dto.request;

import com.sideproject.hororok.category.dto.CategoryKeywords;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CafeFindCategoryRequest {

    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final CategoryKeywords categoryKeywords;

    public CafeFindCategoryRequest(final BigDecimal longitude,
                                   final BigDecimal latitude,
                                   final CategoryKeywords categoryKeywords) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.categoryKeywords = categoryKeywords;
    }
}
