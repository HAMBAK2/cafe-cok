package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryKeywordDto;
import com.sideproject.hororok.utils.enums.PlanMatchType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CreatePlanDto {

    private final PlanMatchType matchType;
    private final List<Cafe> cafe;
    private final List<Cafe> recommendCafes;
    private final List<Cafe> matchCafes;
    private final List<Cafe> similarCafes;
    private final CategoryKeywordDto keywordsByCategory;


    //맞춘 경우
    public static CreatePlanDto of(PlanMatchType matchType, List<Cafe> matchCafes,
                                   List<Cafe> similarCafes, CategoryKeywordDto keywordsByCategory) {

        return CreatePlanDto.builder()
                .matchType(matchType)
                .similarCafes(similarCafes)
                .matchCafes(matchCafes)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }


    //못맞춘경우 , 유사한경우
    public static CreatePlanDto of(PlanMatchType matchType, List<Cafe> similarOrRecommendCafes, CategoryKeywordDto keywordsByCategory) {

        if(matchType.equals(PlanMatchType.SIMILAR)) {
            return CreatePlanDto.builder()
                    .matchType(matchType)
                    .similarCafes(similarOrRecommendCafes)
                    .keywordsByCategory(keywordsByCategory)
                    .build();
        }

        return CreatePlanDto.builder()
                .matchType(matchType)
                .recommendCafes(similarOrRecommendCafes)
                .keywordsByCategory(keywordsByCategory)
                .build();
    }

//
}
