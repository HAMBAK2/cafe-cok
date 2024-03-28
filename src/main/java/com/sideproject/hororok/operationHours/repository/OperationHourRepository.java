package com.sideproject.hororok.operationHours.repository;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {


    @Query("SELECT oh FROM OperationHour oh " +
            "WHERE 1=1 " +
            "AND oh.date = :date " +
                "AND oh.isClosed = false " +
                "AND oh.openingTime <= :endTime " +
                "AND oh.closingTime >= :startTime")
    List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime startTime, java.time.LocalTime endTime);



}
