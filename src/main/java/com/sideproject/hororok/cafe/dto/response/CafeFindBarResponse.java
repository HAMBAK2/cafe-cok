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

    private final boolean isExist;

    //검색한 카페가 존재하지 않는 경우
    private final CategoryKeywordsDto categoryKeywords;
    private final List<CafeDto> cafes;

    //검색한 카페가 존재하는 경우
    private final CafeDetailTopResponse top;
    private final CafeDetailBasicInfoResponse basicInfo;

    public static CafeFindBarResponse notExistOf(final List<CafeDto> cafes,
                                                 final CategoryKeywordsDto categoryKeywords) {

        return CafeFindBarResponse.builder()
                .isExist(false)
                .cafes(cafes)
                .categoryKeywords(categoryKeywords)
                .build();
    }

    public static CafeFindBarResponse existFrom(final CafeDetailTopResponse top,
                                                final CafeDetailBasicInfoResponse basicInfo) {

        return CafeFindBarResponse.builder()
                .isExist(true)
                .top(top)
                .basicInfo(basicInfo)
                .build();
    }

}
