package com.sideproject.cafe_cok.plan.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanEditRequest {

    private String status;

    public PlanEditRequest(final String status) {
        this.status = status;
    }
}
