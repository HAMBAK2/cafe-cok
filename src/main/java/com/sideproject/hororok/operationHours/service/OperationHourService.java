package com.sideproject.hororok.operationHours.service;

import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.operationHours.repository.OperationHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationHourService {

    private final OperationHourRepository operationHourRepository;

    public List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime startTime, LocalTime endTime){

        return operationHourRepository.findOpenHoursByDateAndTimeRange(date, startTime, endTime);
    }

    public Optional<OperationHour> findByCafeIdAndDate(Long cafeId, DayOfWeek date){

        return operationHourRepository.findByCafeIdAndDate(cafeId, date);
    }

    public List<OperationHour> findClosedDayByCafeId(Long cafeId) {
        return operationHourRepository.findClosedDayByCafeId(cafeId);
    }

    public List<OperationHour> findBusinessHoursByCafeId(Long cafeId) {
        return operationHourRepository.findBusinessHoursByCafeId(cafeId);
    }
}
