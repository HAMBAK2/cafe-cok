package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import com.sideproject.cafe_cok.utils.FormatConverter;
import lombok.Builder;
import lombok.Getter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class OperationHourDto {

    private final List<List<String>> hours;

    public static OperationHourDto empty() {

        List<List<String>> emptyHours = new ArrayList<>();
        for(int i = 0; i < 7; i++) emptyHours.add(Arrays.asList("", ""));

        return OperationHourDto.builder()
                .hours(emptyHours)
                .build();
    }

    public static OperationHourDto from(final List<OperationHour> hours) {
        return OperationHourDto.builder()
                .hours(FormatConverter.convertOperationHoursToListString(hours))
                .build();
    }
}
