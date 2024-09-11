package com.sideproject.cafe_cok.plan.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Builder
@Schema(description = "계획 저장 응답")
public class SavePlanResponse extends RepresentationModel<SavePlanResponse> {

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

    @Schema(description = "추천 카페 목록")
    private List<CafeDto> recommendCafes;

    @Schema(description = "일치 카페 목록")
    private List<CafeDto> matchCafes;

    @Schema(description = "유사한 카페 목록")
    private List<CafeDto> similarCafes;

}
