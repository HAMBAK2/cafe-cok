package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.sideproject.hororok.utils.FormatConverter.convertLocalDateLocalTimeToString;

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
            Plan plan, CategoryKeywordsDto categoryKeywords, List<CafeDto> similarCafes) {

        return MyPagePlanDetailResponse.builder()
                .planId(plan.getId())
                .matchType(plan.getMatchType())
                .locationName(plan.getLocationName())
                .minutes(plan.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(plan.getVisitDate(), plan.getVisitStartTime()))
                .categoryKeywords(categoryKeywords)
                .similarCafes(similarCafes)
                .matchCafes(new ArrayList<>())
                .build();
    }

    public static MyPagePlanDetailResponse of(
            Plan plan, CategoryKeywordsDto categoryKeywords, List<CafeDto> similarCafes, List<CafeDto> matchCafes) {

        return MyPagePlanDetailResponse.builder()
                .planId(plan.getId())
                .matchType(plan.getMatchType())
                .locationName(plan.getLocationName())
                .minutes(plan.getMinutes())
                .visitDateTime(convertLocalDateLocalTimeToString(plan.getVisitDate(), plan.getVisitStartTime()))
                .categoryKeywords(categoryKeywords)
                .similarCafes(similarCafes)
                .matchCafes(matchCafes)
                .build();
    }




}
