package com.sideproject.cafe_cok.plan.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "계획 수정/삭제 응답")
public class PlanIdResponse extends RepresentationModel<PlanIdResponse> {

    @Schema(description = "계획 ID", example = "1")
    private Long planId;

    public PlanIdResponse(Long planId) {
        this.planId = planId;
    }
}
