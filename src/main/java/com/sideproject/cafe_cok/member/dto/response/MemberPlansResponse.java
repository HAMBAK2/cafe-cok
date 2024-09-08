package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberPlansResponse extends RepresentationModel<MemberPlansResponse> {

    private List<PlanKeywordDto> plans;

    public MemberPlansResponse(final List<PlanKeywordDto> plans) {
        this.plans = plans;
    }
}
