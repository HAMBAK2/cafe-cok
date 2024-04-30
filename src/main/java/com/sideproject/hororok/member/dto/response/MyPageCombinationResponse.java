package com.sideproject.hororok.member.dto.response;


import com.sideproject.hororok.combination.dto.CombinationDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageCombinationResponse {

    private List<CombinationDto> combinations;

    public static MyPageCombinationResponse from(final List<CombinationDto> combinations) {
        return MyPageCombinationResponse.builder()
                .combinations(combinations)
                .build();
    }
}
