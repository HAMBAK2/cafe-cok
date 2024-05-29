package com.sideproject.cafe_cok.member.dto.response;


import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MyPageCombinationResponse {

    private List<CombinationDto> combinations = new ArrayList<>();

    public MyPageCombinationResponse(final List<CombinationDto> combinations) {
        this.combinations = combinations;
    }
}
