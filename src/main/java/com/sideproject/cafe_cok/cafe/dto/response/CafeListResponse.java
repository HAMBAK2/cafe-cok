package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeListResponse {

    private List<CafeDto> cafes;

    public CafeListResponse(final List<CafeDto> cafes) {
        this.cafes = cafes;
    }
}
