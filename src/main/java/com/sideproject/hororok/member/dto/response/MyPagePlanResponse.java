package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.member.dto.MyPagePlanDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MyPagePlanResponse {

    private List<MyPagePlanDto> plans;

    protected MyPagePlanResponse() {
    }

    public MyPagePlanResponse(final List<MyPagePlanDto> plans) {
        this.plans = plans;
    }
}
