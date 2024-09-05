package com.sideproject.cafe_cok.plan.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.sideproject.cafe_cok.util.FormatConverter.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanKeywordDto {

    private Long id;
    private String location;
    private String visitDateTime;
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
