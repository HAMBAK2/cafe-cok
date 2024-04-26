package com.sideproject.hororok.plan.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.dto.request.CreatePlanRequest;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.sideproject.hororok.utils.FormatConverter.*;

@Getter
public class CreatePlanResponse {

    private Long planId;
    private MatchType matchType;
    private String locationName;
    private Integer minutes;
    private String visitDateTime;
    private CategoryKeywordsDto categoryKeywords;
    private List<CafeDto> recommendCafes = new ArrayList<>();
    private List<CafeDto> matchCafes = new ArrayList<>();
    private List<CafeDto> similarCafes = new ArrayList<>();

    protected CreatePlanResponse() {
    }

    //Match 경우
    public CreatePlanResponse(
            final MatchType matchType, final CreatePlanRequest request, final CategoryKeywordsDto categoryKeywords,
            final List<CafeDto> matchCafes, final List<CafeDto> similarCafes) {

        this.matchType = matchType;
        this.locationName = request.getLocationName();
        this.minutes = request.getMinutes();
        this.visitDateTime = convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime());
        this.categoryKeywords = categoryKeywords;
        this.matchCafes = matchCafes;
        this.similarCafes = similarCafes;
    }

    //SIMILAR / MISMATCH 경우
    public CreatePlanResponse(
            final MatchType matchType, final CreatePlanRequest request,
            final CategoryKeywordsDto categoryKeywords, final List<CafeDto> cafes) {

        this.matchType = matchType;
        this.locationName = request.getLocationName();
        this.minutes = request.getMinutes();
        this.visitDateTime = convertLocalDateLocalTimeToString(request.getDate(), request.getStartTime());
        this.categoryKeywords = categoryKeywords;

        if(matchType.getValue().equals(MatchType.SIMILAR)) {
            this.similarCafes = cafes;
            return;
        }
        this.recommendCafes = cafes;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
