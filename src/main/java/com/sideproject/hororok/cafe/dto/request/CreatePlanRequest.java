package com.sideproject.hororok.cafe.dto.request;


import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CreatePlanRequest {

    private String locationName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer withinMinutes;
    private LocalDate visitDate;
    private LocalTime visitStartTime;
    private LocalTime visitEndTime;
    private List<String> keywords;

    public CreatePlanRequest(
            final String locationName, final BigDecimal longitude, final BigDecimal latitude,
            final Integer withinMinutes, LocalDate visitDate, final LocalTime visitStartTime,
            final LocalTime visitEndTime, final List<String> keywords) {
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.withinMinutes = withinMinutes;
        this.visitDate = visitDate;
        this.visitStartTime = visitStartTime;
        this.visitEndTime = visitEndTime;
        this.keywords = keywords;
    }
}
