package com.sideproject.cafe_cok.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import lombok.Getter;


@Getter
public class CafeHomeResponse {

    @JsonUnwrapped
    private CategoryKeywordsDto categoryKeywords;

    public CafeHomeResponse(final CategoryKeywordsDto categoryKeywords) {
        this.categoryKeywords = categoryKeywords;
    }
}
