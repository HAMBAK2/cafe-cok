package com.sideproject.cafe_cok.plan.dto.request;

import lombok.Getter;

@Getter
public class SharePlanRequest {

    private Long planId;

    private SharePlanRequest() {
    }

    public SharePlanRequest(Long planId) {
        this.planId = planId;
    }
}
