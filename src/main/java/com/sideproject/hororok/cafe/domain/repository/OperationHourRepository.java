package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.OperationHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {


    List<OperationHour> findByDate(DayOfWeek date);

    @Query("SELECT oh FROM OperationHour oh " +
            "WHERE 1=1 " +
            "AND oh.date = :date " +
            "AND oh.isClosed = false " +
            "AND oh.openingTime <= :startTime " +
            "AND oh.closingTime >= :startTime")
    List<OperationHour> findByDateAndStartTime(DayOfWeek date, LocalTime startTime);

    @Query("SELECT oh FROM OperationHour oh " +
            "WHERE 1=1 " +
            "AND oh.date = :date " +
                "AND oh.isClosed = false " +
                "AND oh.openingTime <= :startTime " +
                "AND oh.closingTime >= :endTime")
    List<OperationHour> findOpenHoursByDateAndTimeRange(DayOfWeek date, LocalTime startTime, LocalTime endTime);

    @Query("SELECT oh " +
            "FROM OperationHour oh " +
            "WHERE oh.cafe.id = :cafeId " +
                "AND oh.date = :date " +
                "AND oh.openingTime >= :time " +
                "AND oh.closingTime <= :time")
    Optional<OperationHour> findByCafeIdAndDateAndTime(final Long cafeId, final DayOfWeek date, final LocalTime time);
    
    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.isClosed = true"
    )
    List<OperationHour> findClosedDayByCafeId(final Long cafeId);

    @Query(
            "SELECT oh FROM OperationHour oh " +
                    "WHERE oh.cafe.id = :cafeId " +
                    "AND oh.isClosed = false"
    )
    List<OperationHour> findBusinessHoursByCafeId(Long cafeId);

}
