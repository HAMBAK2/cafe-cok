package com.sideproject.cafe_cok.admin.dto;

import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.utils.FormatConverter;
import lombok.Getter;

import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Getter
public class AdminOperationHourDto {

    private String day;
    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;

    public AdminOperationHourDto(String day) {
        this.day = day;
        this.startHour = 0;
        this.startMinute = 0;
        this.endHour = 0;
        this.endMinute = 0;
    }

    public AdminOperationHourDto(String day,
                                 Integer startHour,
                                 Integer startMinute,
                                 Integer endHour,
                                 Integer endMinute) {
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
