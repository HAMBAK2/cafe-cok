package com.sideproject.cafe_cok.plan.dto.response;

import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanAllResponse {

    private List<PlanKeywordDto> plans;

    public PlanAllResponse(final List<PlanKeywordDto> plans) {
        this.plans = plans;
    }
}
