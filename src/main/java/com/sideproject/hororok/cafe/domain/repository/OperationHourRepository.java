package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.OperationHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {

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
