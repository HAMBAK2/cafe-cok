package com.sideproject.hororok.cafe.cond;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreatePlanSearchCond {

    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer minutes;
    private String day;
    private Integer startHour;
    private Integer endHour;
    private String purpose;
    private String menu;
    private String theme;
    private String facility;
    private String mood;
}
