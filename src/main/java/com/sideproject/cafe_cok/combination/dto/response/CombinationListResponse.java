package com.sideproject.cafe_cok.combination.dto.response;


import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CombinationListResponse {

    private List<CombinationDto> combinations = new ArrayList<>();

    public CombinationListResponse(final List<CombinationDto> combinations) {
        this.combinations = combinations;
    }
}
