package com.sideproject.hororok.member.dto.response;

import com.sideproject.hororok.member.dto.MyPagePlanDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPagePlansAllResponse {

    private List<MyPagePlanDto> plans;

    public static  MyPagePlansAllResponse from(final List<MyPagePlanDto> plans) {
        return MyPagePlansAllResponse.builder()
                .plans(plans)
                .build();
    }
}
