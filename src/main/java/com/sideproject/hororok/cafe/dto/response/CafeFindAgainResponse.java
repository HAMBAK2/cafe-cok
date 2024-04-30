package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
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
