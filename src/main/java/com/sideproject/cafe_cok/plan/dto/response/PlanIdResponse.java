package com.sideproject.cafe_cok.plan.dto.response;

import lombok.Getter;

@Getter
public class PlanIdResponse {

    private Long planId;

    protected PlanIdResponse() {
    }

    public PlanIdResponse(Long planId) {
        this.planId = planId;
    }
}
