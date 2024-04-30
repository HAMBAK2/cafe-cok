package com.sideproject.hororok.combination.dto.response;


import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CombinationDetailResponse {

    private Long id;
    private String name;
    private String icon;
    private CategoryKeywordsDto categoryKeywords;

    public static CombinationDetailResponse of(final Combination combination, final CategoryKeywordsDto categoryKeywords) {

        return CombinationDetailResponse.builder()
                .id(combination.getId())
                .name(combination.getName())
                .icon(combination.getIcon())
                .categoryKeywords(categoryKeywords)
                .build();
    }

}
