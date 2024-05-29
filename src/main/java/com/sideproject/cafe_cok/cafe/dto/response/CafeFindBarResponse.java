package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeFindBarResponse {


    private List<CafeDto> cafes;

    public CafeFindBarResponse(final List<CafeDto> cafes) {
        this.cafes = cafes;
    }
}
