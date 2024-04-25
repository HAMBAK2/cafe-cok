package com.sideproject.hororok.plan.dto;

import com.sideproject.hororok.plan.domain.Plan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SavedPlanDto {

    private final Long id;
    private final String purpose;
    private final String location;
    private final String visitDateTime;
    private final LocalDateTime createdDate;

    public static SavedPlanDto of(final Plan plan, final String purpose) {
        return SavedPlanDto.builder()
                .id(plan.getId())
                .purpose(purpose)
                .location(plan.getLocationName())
                .visitDateTime(plan.getVisitDateTime())
                .createdDate(plan.getCreatedDate())
                .build();
    }

}
