package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MyPagePlanDetailResponse {

    private final Long planId;
    private final MatchType matchType;
    private final String locationName;
    private final Integer minutes;
    private final String visitDateTime;
    private final CategoryKeywordsDto categoryKeywords;
    private final List<CafeDto> matchCafes;
    private final List<CafeDto> similarCafes;

    public static MyPagePlanDetailResponse of(
            Plan plan, CategoryKeywordsDto categoryKeywords, List<CafeDto> cafes) {

        MyPagePlanDetailResponseBuilder builder = MyPagePlanDetailResponse.builder()
                .planId(plan.getId())
                .matchType(plan.getMatchType())
                .locationName(plan.getLocationName())
                .minutes(plan.getMinutes())
                .visitDateTime(plan.getVisitDateTime())
                .categoryKeywords(categoryKeywords);

        if(plan.getMatchType().equals(MatchType.MATCH)) builder.matchCafes = cafes;
        else if(plan.getMatchType().equals(MatchType.SIMILAR)) builder.similarCafes(cafes);

        return builder.build();
    }




}
