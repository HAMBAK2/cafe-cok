package com.sideproject.cafe_cok.cafe.condition;

import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CafeSearchCondition {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int minutes;

    public CafeSearchCondition(final CreatePlanRequest request) {
        this.date = request.getDate();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.minutes = request.getMinutes();
    }

}
