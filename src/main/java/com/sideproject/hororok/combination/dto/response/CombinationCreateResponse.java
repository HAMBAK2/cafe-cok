package com.sideproject.hororok.combination.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CombinationCreateResponse {

    private Long combinationId;

    public static CombinationCreateResponse of(final Long combinationId) {
        return CombinationCreateResponse.builder()
                .combinationId(combinationId)
                .build();
    }
}
