package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeFindAgainResponse {

    private List<CafeDto> cafes;

    public CafeFindAgainResponse(final List<CafeDto> cafes) {
        this.cafes = cafes;
    }
}
