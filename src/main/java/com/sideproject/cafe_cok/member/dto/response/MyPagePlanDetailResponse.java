package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.dto.CafeBookmarkImageDto;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.sideproject.cafe_cok.utils.FormatConverter.convertLocalDateLocalTimeToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPagePlanDetailResponse {

    private Long planId;
    private MatchType matchType;
    private String locationName;
    private Integer minutes;
    private String visitDateTime;
    private CategoryKeywordsDto categoryKeywords;
    private List<CafeBookmarkImageDto> matchCafes = new ArrayList<>();
    private List<CafeBookmarkImageDto> similarCafes = new ArrayList<>();

    public MyPagePlanDetailResponse(final Plan plan,
                                    final CategoryKeywordsDto categoryKeywords,
                                    final List<CafeBookmarkImageDto> similarCafes,
                                    final List<CafeBookmarkImageDto> matchCafes) {

        this.planId = plan.getId();
        this.matchType = plan.getMatchType();
        this.locationName = plan.getLocationName();
        this.minutes = plan.getMinutes();
        this.visitDateTime = convertLocalDateLocalTimeToString(plan.getVisitDate(), plan.getVisitStartTime());
        this.categoryKeywords = categoryKeywords;
        this.similarCafes = similarCafes;
        this.matchCafes = matchCafes;
    }

}
