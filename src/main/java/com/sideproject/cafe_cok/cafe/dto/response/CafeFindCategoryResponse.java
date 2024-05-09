package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeFindCategoryResponse {

    private final List<CafeDto> cafes;

    public static CafeFindCategoryResponse from(final List<CafeDto> cafes) {
        return CafeFindCategoryResponse.builder()
                .cafes(cafes)
                .build();
    }
}
