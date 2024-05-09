package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CafeFindAgainResponse {

    private final List<CafeDto> cafes;

    public static CafeFindAgainResponse from(List<CafeDto> cafes) {
        return CafeFindAgainResponse.builder()
                .cafes(cafes)
                .build();
    }
}
