package com.sideproject.cafe_cok.plan.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletePlanResponse {

    private Long planId;

    public DeletePlanResponse(Long planId) {
        this.planId = planId;
    }
}
