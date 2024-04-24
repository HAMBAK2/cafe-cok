package com.sideproject.hororok.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import lombok.Getter;


@Getter
public class CafeHomeResponse {

    @JsonUnwrapped
    private CategoryKeywordsDto categoryKeywords;

    public CafeHomeResponse(final CategoryKeywordsDto categoryKeywords) {
        this.categoryKeywords = categoryKeywords;
    }
}
