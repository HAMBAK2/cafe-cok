package com.sideproject.cafe_cok.plan.dto.response;

import lombok.Getter;

@Getter
public class SavePlanResponse {

    private Long planId;

    protected SavePlanResponse() {
    }

    public SavePlanResponse(Long planId) {
        this.planId = planId;
    }
}
