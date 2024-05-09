package com.sideproject.cafe_cok.member.dto.response;


import com.sideproject.cafe_cok.combination.dto.CombinationDto;
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
