package com.sideproject.hororok.cafe.dto.response;

import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
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
