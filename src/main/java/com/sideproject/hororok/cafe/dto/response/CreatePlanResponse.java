package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.cafe.dto.request.CreatePlanRequest;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.plan.domain.enums.PlanResult;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CreatePlanResponse {

    private PlanResult planResult;
    private String locationName;
    private Integer withinMinutes;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private CategoryKeywordsDto categoryKeywords;
    private List<CafeDto> recommendCafes = new ArrayList<>();
    private List<CafeDto> matchCafes = new ArrayList<>();
    private List<CafeDto> similarCafes = new ArrayList<>();

    protected CreatePlanResponse() {
    }

    //Match인 경우
    public CreatePlanResponse(
            final PlanResult planResult, final CreatePlanRequest request, final CategoryKeywordsDto categoryKeywords,
            final List<CafeDto> matchCafes, final List<CafeDto> similarCafes) {

        this.planResult = planResult;
        this.locationName = request.getLocationName();
        this.withinMinutes = request.getWithinMinutes();
        this.visitDate = request.getVisitDate();
        this.visitTime = request.getVisitStartTime();
        this.categoryKeywords = categoryKeywords;
        this.matchCafes = matchCafes;
        this.similarCafes = similarCafes;
    }

    //SIMILAR / MISMATCH 경우
    public CreatePlanResponse(
            final PlanResult planResult, final CreatePlanRequest request,
            final CategoryKeywordsDto categoryKeywords, final List<CafeDto> cafes) {

        this.planResult = planResult;
        this.locationName = request.getLocationName();
        this.withinMinutes = request.getWithinMinutes();
        this.visitDate = request.getVisitDate();
        this.visitTime = request.getVisitStartTime();
        this.categoryKeywords = categoryKeywords;

        if(planResult.getValue().equals(PlanResult.SIMILAR)) {
            this.similarCafes = cafes;
            return;
        }
        this.recommendCafes = cafes;
    }
}
