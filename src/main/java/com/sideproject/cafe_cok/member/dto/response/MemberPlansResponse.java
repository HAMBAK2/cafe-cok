package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "회원 계획 조회 응답")
public class MemberPlansResponse extends RepresentationModel<MemberPlansResponse> {

    @Schema(description = "회원 계획 DTO 리스트")
    private List<PlanKeywordDto> plans;

    public MemberPlansResponse(final List<PlanKeywordDto> plans) {
        this.plans = plans;
    }
}
