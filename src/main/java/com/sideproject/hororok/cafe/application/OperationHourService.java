package com.sideproject.hororok.cafe.application;

import com.sideproject.hororok.cafe.domain.OpenStatus;
import com.sideproject.hororok.cafe.domain.OperationHour;
import com.sideproject.hororok.cafe.domain.OperationHourRepository;
import com.sideproject.hororok.utils.converter.FormatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationHourService {

    private final OperationHourRepository operationHourRepository;

    
    public OpenStatus getOpenStatus(Long cafeId){

        LocalTime time = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        OperationHour operationHour = findByCafeIdAndDate(cafeId, today).get();

        if(operationHour.isClosed()) return OpenStatus.HOLY_DAY;

        if(time.isAfter(operationHour.getOpeningTime()) && time.isBefore(operationHour.getClosingTime())) {
            return OpenStatus.OPEN;
        }

        return OpenStatus.OPEN;
    }

    
    public List<String> getClosedDay(Long cafeId) {


        List<OperationHour> findDays = findClosedDayByCafeId(cafeId);
        List<String> closeDays = new ArrayList<>();

        if(findDays.isEmpty()) return closeDays;

        for (OperationHour findDay : findDays) {
            DayOfWeek date = findDay.getDate();
            closeDays.add(FormatConverter.getKoreanDayOfWeek(date));
        }

        return closeDays;
    }

    
    public List<String> getBusinessHours(Long cafeId) {

        List<OperationHour> businessHours = findBusinessHoursByCafeId(cafeId);
        List<String> convertedBusinessHours = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (OperationHour businessHour : businessHours) {
            String input;
            String date = FormatConverter.getKoreanDayOfWeek(businessHour.getDate());
            String openingTime = businessHour.getOpeningTime().format(formatter);
            String closingTime = businessHour.getClosingTime().format(formatter);

            input = date + " " + openingTime + "~" + closingTime;
            convertedBusinessHours.add(input);
        }

        return convertedBusinessHours;
    }

    
    private Optional<OperationHour> findByCafeIdAndDate(Long cafeId, DayOfWeek date){

        return operationHourRepository.findByCafeIdAndDate(cafeId, date);
    }

    
    private List<OperationHour> findClosedDayByCafeId(Long cafeId) {
        return operationHourRepository.findClosedDayByCafeId(cafeId);
    }

    
    private List<OperationHour> findBusinessHoursByCafeId(Long cafeId) {
        return operationHourRepository.findBusinessHoursByCafeId(cafeId);
    }
}
