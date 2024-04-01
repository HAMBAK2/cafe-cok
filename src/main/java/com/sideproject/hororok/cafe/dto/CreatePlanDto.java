package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryAndKeyword;
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
    private final String locationName;
    private final Integer minutes;
    private final List<CategoryAndKeyword> keywords;
    private final List<Cafe> cafe;
    private final List<Cafe> recommendCafes;
    private final List<Cafe> matchCafes;
    private final List<Cafe> similarCafes;


    //맞춘 경우
    public static CreatePlanDto of(PlanMatchType matchType, CreatePlanSearchCond searchCond,
                                   List<Cafe> matchCafes, List<Cafe> similarCafes) {

        return CreatePlanDto.builder()
                .matchType(matchType)
                .locationName(searchCond.getLocationName())
                .minutes(searchCond.getMinutes())
                .keywords(searchCond.getKeywords())
                .similarCafes(similarCafes)
                .matchCafes(matchCafes)
                .build();
    }


    //못맞춘경우 , 유사한경우
    public static CreatePlanDto of(PlanMatchType matchType, CreatePlanSearchCond searchCond, List<Cafe> cafes) {

        if(matchType.equals(PlanMatchType.SIMILAR)) {
            return CreatePlanDto.builder()
                    .matchType(matchType)
                    .locationName(searchCond.getLocationName())
                    .minutes(searchCond.getMinutes())
                    .keywords(searchCond.getKeywords())
                    .similarCafes(cafes)
                    .build();
        }

        return CreatePlanDto.builder()
                .matchType(matchType)
                .locationName(searchCond.getLocationName())
                .minutes(searchCond.getMinutes())
                .keywords(searchCond.getKeywords())
                .recommendCafes(cafes)
                .build();
    }

//
}
