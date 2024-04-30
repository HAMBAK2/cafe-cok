package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.OperationHour;
import com.sideproject.hororok.cafe.exception.NoSuchCafeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    default Cafe getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchCafeException::new);
    }

    Optional<Cafe> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    
    List<Cafe> findAllByOrderByStarRatingDescNameAsc();

    List<Cafe> findByIdIn(List<Long> ids);

    @Query("SELECT c " +
            "FROM Cafe c " +
                "JOIN OperationHour oh ON c.id = oh.cafe.id " +
            "WHERE oh.date = :date " +
                "AND oh.isClosed = false " +
                "AND oh.openingTime <= :startTime " +
                "AND oh.closingTime >= :startTime")
    List<Cafe> findByDateAndStartTime(final DayOfWeek date, final LocalTime startTime);

    @Query("SELECT c " +
            "FROM Cafe c " +
                "JOIN OperationHour oh ON c.id = oh.cafe.id " +
            "WHERE oh.date = :date")
    List<Cafe> findByDate(final DayOfWeek date);

    @Query("SELECT c FROM Cafe c " +
                "JOIN OperationHour oh ON c.id = oh.cafe.id " +
            "WHERE oh.date = :date " +
                "AND oh.isClosed = false " +
                "AND oh.openingTime <= :startTime " +
                "AND oh.closingTime >= :endTime")
    List<Cafe> findOpenHoursByDateAndTimeRange
            (final DayOfWeek date, final LocalTime startTime, final LocalTime endTime);
}
