package com.sideproject.hororok.operationHours.service;

import com.sideproject.hororok.operationHours.dto.BusinessHour;
import com.sideproject.hororok.operationHours.dto.BusinessScheduleDto;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.operationHours.repository.OperationHourRepository;
import com.sideproject.hororok.utils.converter.FormatConverter;
import com.sideproject.hororok.utils.enums.OpenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationHourService {

    private final OperationHourRepository operationHourRepository;


    public List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime startTime, LocalTime endTime){

        return operationHourRepository.findOpenHoursByDateAndTimeRange(date, startTime, endTime);
    }

    public BusinessScheduleDto getWorkTimeInfo(Long cafeId) {

        List<OperationHour> operationHours = operationHourRepository.findOperationHourByCafeId(cafeId);

        List<OperationHour> sortedOperationHours = operationHours.stream()
                .sorted(Comparator.comparing(OperationHour::getDate))
                .collect(Collectors.toList());


        List<BusinessHour> businessHours = new ArrayList<>();
        List<String> closedDays = new ArrayList<>();
        DayOfWeek todayOfWeek = LocalDate.now().getDayOfWeek();

        DayOfWeek targetDay;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (OperationHour sortedOperationHour : sortedOperationHours) {

            targetDay = sortedOperationHour.getDate();
            if(sortedOperationHour.isClosed()) {
                closedDays.add(FormatConverter
                        .convertDayOfWeekEnglishToKoreanString(sortedOperationHour.getDate()));
            }

            BusinessHour businessHour = BusinessHour.of(
                    FormatConverter.convertDayOfWeekEnglishToKoreanString(targetDay),
                    sortedOperationHour.getOpeningTime().format(formatter),
                    sortedOperationHour.getClosingTime().format(formatter));

            if(targetDay.equals(todayOfWeek)) {
                businessHours.add(0, businessHour);
                continue;
            }

            businessHours.add(businessHour);
        }

        return BusinessScheduleDto.of(businessHours, closedDays, getOpenStatus(businessHours.get(0), todayOfWeek));
    }

    private OpenStatus getOpenStatus(BusinessHour businessHour, DayOfWeek todayOfWeek) {

        String date = businessHour.getDate();
        if(!todayOfWeek.equals(date)) {
            return OpenStatus.HOLY_DAY;
        }

        LocalTime openingTime = LocalTime.parse(businessHour.getOpeningTime());
        LocalTime closingTime = LocalTime.parse(businessHour.getClosingTime());

        LocalTime now = LocalTime.now();
        if(openingTime.isBefore(now) && closingTime.isAfter(now)) return OpenStatus.OPEN;


        return OpenStatus.CLOSE;
    }

}
