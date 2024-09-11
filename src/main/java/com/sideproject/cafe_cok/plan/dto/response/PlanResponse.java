package com.sideproject.cafe_cok.plan.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

import static com.sideproject.cafe_cok.util.FormatConverter.convertLocalDateLocalTimeToString;

@Getter
@NoArgsConstructor
@Schema(description = "계획 조회 응답")
public class PlanResponse extends RepresentationModel<PlanResponse> {

    @Schema(description = "계획 ID", example = "1")
    private Long planId;

    @Schema(description = "계획 일치 타입", example = "MATCH")
    private MatchType matchType;

    @Schema(description = "계획 장소명", example = "망원역")
    private String locationName;

    @Schema(description = "계획한 도보거리", example = "30")
    private Integer minutes;

    @Schema(description = "계획한 방문일시", example = "9월 11일 10시 0분")
    private String visitDateTime;

    @Schema(description = "카테고리 별 키워드")
    private CategoryKeywordsDto categoryKeywords;

    @Schema(description = "일치 카페 목록")
    private List<CafeDto> matchCafes = new ArrayList<>();

    @Schema(description = "유사한 카페 목록")
    private List<CafeDto> similarCafes = new ArrayList<>();

    public PlanResponse(final Plan plan,
                        final CategoryKeywordsDto categoryKeywords,
                        final List<CafeDto> similarCafes,
                        final List<CafeDto> matchCafes) {

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
