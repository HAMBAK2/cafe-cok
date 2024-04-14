package com.sideproject.hororok.cafe.domain;

import com.sideproject.hororok.aop.annotation.LogTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {


    @LogTrace
    @Query("SELECT oh FROM OperationHour oh " +
            "WHERE 1=1 " +
            "AND oh.date = :date " +
                "AND oh.isClosed = false " +
                "AND oh.openingTime <= :startTime " +
                "AND oh.closingTime >= :endTime")
    List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime startTime, LocalTime endTime);

    @LogTrace
    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.date = :date"
    )
    Optional<OperationHour> findByCafeIdAndDate(Long cafeId, DayOfWeek date);

    @LogTrace
    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.isClosed = true"
    )
    List<OperationHour> findClosedDayByCafeId(Long cafeId);

    @LogTrace
    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.isClosed = false"
    )
    List<OperationHour> findBusinessHoursByCafeId(Long cafeId);

}
