package com.sideproject.cafe_cok.plan.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.sideproject.cafe_cok.util.FormatConverter.*;

@Getter
@NoArgsConstructor
@Schema(description = "회원 계획 조회 DTO")
public class PlanKeywordDto {

    @Schema(description = "계획 ID", example = "1")
    private Long id;

    @Schema(description = "검색한 장소명", example = "망원역")
    private String location;

    @Schema(description = "방문 시간", example = "9월 7일 10시 0분")
    private String visitDateTime;

    @Schema(description = "대표 키워드 명", example = "데이트/모임")
    private String keywordName;

    @QueryProjection
    public PlanKeywordDto(final Long id,
                          final String location,
                          final LocalDate visitDate,
                          final LocalTime visitStartTime,
                          final String keywordName) {
        this.id = id;
        this.location = location;
        this.visitDateTime = convertLocalDateLocalTimeToString(visitDate, visitStartTime);
        this.keywordName = keywordName;
    }

}
