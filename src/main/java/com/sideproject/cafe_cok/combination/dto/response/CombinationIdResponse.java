package com.sideproject.cafe_cok.combination.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class CombinationIdResponse extends RepresentationModel<CombinationListResponse> {

    private Long combinationId;

    public CombinationIdResponse(final Long combinationId) {
        this.combinationId = combinationId;
    }
}
