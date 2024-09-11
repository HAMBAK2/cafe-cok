package com.sideproject.cafe_cok.combination.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "조합 저장/수정 응답")
public class CombinationIdResponse extends RepresentationModel<CombinationListResponse> {

    @Schema(description = "조합 ID", example = "1")
    private Long combinationId;

    public CombinationIdResponse(final Long combinationId) {
        this.combinationId = combinationId;
    }
}
