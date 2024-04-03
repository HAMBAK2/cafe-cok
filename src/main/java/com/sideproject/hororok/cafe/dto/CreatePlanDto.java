package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.category.dto.CategoryKeywords;
import com.sideproject.hororok.plan.enums.PlanMatchType;
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
    private final String visitDateTime;
    private final CategoryKeywords categoryKeywords;
    private final List<CafeDto> recommendCafes;
    private final List<CafeDto> matchCafes;
    private final List<CafeDto> similarCafes;


    //맞춘 경우
    public static CreatePlanDto of(PlanMatchType matchType, CreatePlanSearchCond searchCond,
                                   String visitDateTime, List<CafeDto> matchCafes, List<CafeDto> similarCafes) {

        return CreatePlanDto.builder()
                .matchType(matchType)
                .locationName(searchCond.getLocationName())
                .minutes(searchCond.getMinutes())
                .visitDateTime(visitDateTime)
                .categoryKeywords(searchCond.getCategoryKeywords())
                .similarCafes(similarCafes)
                .matchCafes(matchCafes)
                .build();
    }


    //못맞춘경우 , 유사한경우
    public static CreatePlanDto of(PlanMatchType matchType, CreatePlanSearchCond searchCond,
                                   String visitDateTime, List<CafeDto> cafes) {

        if(matchType.equals(PlanMatchType.SIMILAR)) {
            return CreatePlanDto.builder()
                    .matchType(matchType)
                    .locationName(searchCond.getLocationName())
                    .minutes(searchCond.getMinutes())
                    .visitDateTime(visitDateTime)
                    .categoryKeywords(searchCond.getCategoryKeywords())
                    .similarCafes(cafes)
                    .build();
        }

        return CreatePlanDto.builder()
                .matchType(matchType)
                .locationName(searchCond.getLocationName())
                .minutes(searchCond.getMinutes())
                .visitDateTime(visitDateTime)
                .categoryKeywords(searchCond.getCategoryKeywords())
                .recommendCafes(cafes)
                .build();
    }

}
