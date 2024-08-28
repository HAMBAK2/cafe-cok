package com.sideproject.cafe_cok.plan.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SavePlanResponse {

    private Long planId;
    private MatchType matchType;
    private String locationName;
    private Integer minutes;
    private String visitDateTime;
    private CategoryKeywordsDto categoryKeywords;
    private List<CafeDto> recommendCafes;
    private List<CafeDto> matchCafes;
    private List<CafeDto> similarCafes;

}
