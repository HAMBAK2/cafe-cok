package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.plan.dto.PlanDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPagePlanResponse {

    private final List<PlanDto> savedPlans;
    private final List<PlanDto> sharedPlans;

    public static MyPagePlanResponse from(final List<PlanDto> savedPlans,
                                          final List<PlanDto> sharedPlans) {

        return MyPagePlanResponse.builder()
                .savedPlans(savedPlans)
                .sharedPlans(sharedPlans)
                .build();
    }

}
