package com.sideproject.hororok.cafe.cond;


import com.sideproject.hororok.category.dto.CategoryAndKeyword;
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

    private final String locationName;
    private final BigDecimal longitude;
    private final BigDecimal latitude;
    private final Integer minutes;
    private final String date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final List<CategoryAndKeyword> categoryKeywords;


    public static CreatePlanSearchCond of(String locationName, BigDecimal longitude, BigDecimal latitude, Integer minutes,
                                          String date, LocalTime startTime, LocalTime endTime, List<CategoryAndKeyword> categoryKeywords) {

        return CreatePlanSearchCond.builder()
                .locationName(locationName)
                .longitude(longitude)
                .latitude(latitude)
                .minutes(minutes)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .categoryKeywords(categoryKeywords)
                .build();
    }

}
