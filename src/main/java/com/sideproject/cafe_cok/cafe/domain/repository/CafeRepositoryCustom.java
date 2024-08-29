package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.condition.CafeSearchCondition;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CafeRepositoryCustom {

    List<Cafe> findByDateAndTime(final CafeSearchCondition searchCondition,
                                 final Sort sort);
    List<Cafe> findByDateAndTimeAndMinutes(final CafeSearchCondition searchCondition);

    List<Cafe> findByPlanIdAndMatchType(final Long planId,
                                        final MatchType matchType);

    List<Cafe> findNearestCafes(final BigDecimal latitude,
                                final BigDecimal longitude);

    List<Cafe> findByDate(final LocalDate date,
                          final Sort sort,
                          final Integer limit);
}
