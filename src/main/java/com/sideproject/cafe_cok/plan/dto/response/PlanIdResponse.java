package com.sideproject.cafe_cok.plan.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
public class PlanIdResponse extends RepresentationModel<PlanIdResponse> {

    private Long planId;

    public PlanIdResponse(Long planId) {
        this.planId = planId;
    }
}
