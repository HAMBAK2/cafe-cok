package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.plan.dto.SavedPlanDto;
import com.sideproject.hororok.plan.dto.SharedPlanDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPagePlanResponse {

    private final List<SavedPlanDto> savedPlans;
    private final List<SharedPlanDto> sharedPlans;

    public static MyPagePlanResponse from(final List<SavedPlanDto> savedPlans,
                                          final List<SharedPlanDto> sharedPlans) {

        return MyPagePlanResponse.builder()
                .savedPlans(savedPlans)
                .sharedPlans(sharedPlans)
                .build();
    }

}
