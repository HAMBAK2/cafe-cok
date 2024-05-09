package com.sideproject.cafe_cok.plan.dto.request;

import lombok.Getter;

@Getter
public class SavePlanRequest {

    private Long planId;

    private SavePlanRequest() {
    }

    public SavePlanRequest(Long planId) {
        this.planId = planId;
    }
}
