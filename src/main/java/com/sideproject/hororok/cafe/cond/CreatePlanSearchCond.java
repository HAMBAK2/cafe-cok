package com.sideproject.hororok.cafe.cond;


import lombok.Getter;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CreatePlanSearchCond {

    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer minutes;
    private String date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> keywords;
}
