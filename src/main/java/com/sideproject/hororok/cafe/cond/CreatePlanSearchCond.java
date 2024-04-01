package com.sideproject.hororok.cafe.cond;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class CreatePlanSearchCond {

    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final Integer minutes;
    private final String date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final List<String> keywords;


    public static CreatePlanSearchCond of(BigDecimal longitude, BigDecimal latitude, Integer minutes, String date,
                                          LocalTime startTime, LocalTime endTime, List<String> keywords) {

        return CreatePlanSearchCond.builder()
                .longitude(longitude)
                .latitude(latitude)
                .minutes(minutes)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .keywords(keywords)
                .build();
    }

}
