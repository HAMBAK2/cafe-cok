package com.sideproject.hororok.plan.dto.request;

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
