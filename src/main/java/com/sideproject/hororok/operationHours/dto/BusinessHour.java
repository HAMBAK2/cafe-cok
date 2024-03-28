package com.sideproject.hororok.operationHours.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class BusinessHour {

    private final String date;
    private final String openingTime;
    private final String closingTime;

    public static BusinessHour of(String date, String openingTime, String closingTime) {

        return BusinessHour.builder()
                .date(date)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .build();
    }

}
