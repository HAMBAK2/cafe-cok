package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CafeOperationHourDto {

    private String day;
    private Integer startHour = 0;
    private Integer startMinute = 0;
    private Integer endHour = 0;
    private Integer endMinute = 0;

    @Builder
    public CafeOperationHourDto(final String day,
                                final Integer startHour,
                                final Integer startMinute,
                                final Integer endHour,
                                final Integer endMinute) {
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public void changeTime(OperationHour operationHour) {
        this.startHour = operationHour.getOpeningTime().getHour();
        this.startMinute = operationHour.getOpeningTime().getMinute();
        this.endHour = operationHour.getClosingTime().getHour();
        this.endMinute = operationHour.getClosingTime().getMinute();
    }

}
