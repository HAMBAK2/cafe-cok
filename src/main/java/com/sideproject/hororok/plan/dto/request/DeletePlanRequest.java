package com.sideproject.hororok.plan.dto.request;

import com.sideproject.hororok.plan.domain.enums.PlanStatus;
import lombok.Getter;

@Getter
public class DeletePlanRequest {

    private PlanStatus planStatus;

    protected DeletePlanRequest() {
    }

    public DeletePlanRequest(PlanStatus planStatus) {
        this.planStatus = planStatus;
    }
}
