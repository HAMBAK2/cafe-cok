package com.sideproject.cafe_cok.plan.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class PlanIdResponse extends RepresentationModel<PlanIdResponse> {

    private Long planId;

    protected PlanIdResponse() {
    }

    public PlanIdResponse(Long planId) {
        this.planId = planId;
    }
}
