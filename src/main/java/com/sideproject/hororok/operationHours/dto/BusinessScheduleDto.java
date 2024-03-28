package com.sideproject.hororok.operationHours.dto;

import com.sideproject.hororok.utils.enums.OpenStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class BusinessScheduleDto {

    private final List<BusinessHour> businessHours;
    private final List<String> closedDay;
    private final String openStatus;

    public static BusinessScheduleDto of(List<BusinessHour> businessHours, List<String> closedDay,OpenStatus openStatus) {
        return BusinessScheduleDto.builder()
                .businessHours(businessHours)
                .closedDay(closedDay)
                .openStatus(openStatus.toString())
                .build();
    }
}
