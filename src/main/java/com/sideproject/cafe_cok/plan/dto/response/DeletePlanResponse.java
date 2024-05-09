package com.sideproject.cafe_cok.plan.dto.response;

import lombok.Getter;

@Getter
public class DeletePlanResponse {

    private Long planId;

    public DeletePlanResponse() {
    }

    public DeletePlanResponse(Long planId) {
        this.planId = planId;
    }
}
