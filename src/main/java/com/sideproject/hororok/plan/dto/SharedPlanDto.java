package com.sideproject.hororok.plan.dto;

import com.sideproject.hororok.plan.domain.Plan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SharedPlanDto {

    private final Long id;
    private final String purpose;
    private final String location;
    private final String visitDateTime;
    private final LocalDateTime createdDate;

    public static SharedPlanDto of(final Plan plan, final String purpose) {
        return SharedPlanDto.builder()
                .id(plan.getId())
                .purpose(purpose)
                .location(plan.getLocationName())
                .visitDateTime(plan.getVisitDateTime())
                .createdDate(plan.getCreatedDate())
                .build();
    }

}
