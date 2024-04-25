package com.sideproject.hororok.plan.dto;

import com.sideproject.hororok.plan.domain.Plan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanDto {

    private final Long id;
    private final String purpose;
    private final String location;
    private final String visitDateTime;

    public static PlanDto of(final Plan plan, final String purpose) {
        return PlanDto.builder()
                .id(plan.getId())
                .purpose(purpose)
                .location(plan.getLocationName())
                .visitDateTime(plan.getVisitDateTime())
                .build();
    }

}
