package com.sideproject.hororok.operationHours.service;

import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.operationHours.repository.OperationHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationHourService {

    private final OperationHourRepository operationHourRepository;

    public List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime startTime, LocalTime endTime){

        return operationHourRepository.findOpenHoursByDateAndTimeRange(date, startTime, endTime);
    }
}
