package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.member.dto.MyPagePlanDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MyPagePlansResponse {

    private Integer page;
    private List<MyPagePlanDto> plans;

    protected MyPagePlansResponse() {
    }

    public MyPagePlansResponse(final Integer page, final List<MyPagePlanDto> plans) {
        this.page = page;
        this.plans = plans;
    }
}
