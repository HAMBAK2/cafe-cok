package com.sideproject.cafe_cok.combination.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
public class CombinationIdResponse extends RepresentationModel<CombinationListResponse> {

    private Long combinationId;

    public static CombinationIdResponse of(final Long combinationId) {
        return CombinationIdResponse.builder()
                .combinationId(combinationId)
                .build();
    }
}
