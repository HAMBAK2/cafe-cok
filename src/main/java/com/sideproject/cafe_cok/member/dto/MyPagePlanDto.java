package com.sideproject.cafe_cok.member.dto;

import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.utils.FormatConverter;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MyPagePlanDto {

    @NotNull
    private final Long id;
    @NotNull
    private final String keywordName;
    private final String location;
    private final String visitDateTime;

    public static MyPagePlanDto of(final Plan plan, final String keywordName) {
        return MyPagePlanDto.builder()
                .id(plan.getId())
                .keywordName(keywordName)
                .location(plan.getLocationName())
                .visitDateTime(FormatConverter.convertLocalDateLocalTimeToString(plan.getVisitDate(), plan.getVisitStartTime()))
                .build();
    }

}