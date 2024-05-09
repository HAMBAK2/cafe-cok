package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeFindBarResponse {


    private final List<CafeDto> cafes;

    public static CafeFindBarResponse from(final List<CafeDto> cafes){

        return CafeFindBarResponse.builder()
                .cafes(cafes)
                .build();
    }
}
