package com.sideproject.hororok.member.dto;

import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.dto.PlanDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.sideproject.hororok.utils.FormatConverter.convertLocalDateLocalTimeToString;


@Getter
@Builder
public class MyPagePlanDto {

    @NotNull
    private final Long id;
    @NotNull
    private final KeywordDto keyword;
    private final String location;
    private final String visitDateTime;

    public static MyPagePlanDto of(final Plan plan, final KeywordDto keyword) {
        return MyPagePlanDto.builder()
                .id(plan.getId())
                .keyword(keyword)
                .location(plan.getLocationName())
                .visitDateTime(convertLocalDateLocalTimeToString(plan.getVisitDate(), plan.getVisitStartTime()))
                .build();
    }

}