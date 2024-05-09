package com.sideproject.cafe_cok.plan.dto.response;

import lombok.Getter;

@Getter
public class SharePlanResponse {

    private Long planId;

    protected SharePlanResponse() {
    }

    public SharePlanResponse(Long planId) {
        this.planId = planId;
    }
}
