package com.sideproject.hororok.operationHours.repository;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {


    @Query("SELECT oh FROM OperationHour oh " +
            "WHERE 1=1 " +
            "AND oh.date = :date " +
                "AND oh.isClosed = false " +
                "AND oh.openingTime <= :visitTime " +
                "AND oh.closingTime >= :visitTime")
    List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime visitTime);


    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.date = :date"
    )
    Optional<OperationHour> findByCafeIdAndDate(Long cafeId, DayOfWeek date);

    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.isClosed = true"
    )
    List<OperationHour> findClosedDayByCafeId(Long cafeId);

    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.isClosed = false"
    )
    List<OperationHour> findBusinessHoursByCafeId(Long cafeId);

}
