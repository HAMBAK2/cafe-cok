package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CafeRepositoryCustom {


    List<Cafe> findByDateAndTimeOrderByStarRatingDesc(final CreatePlanRequest request);
    List<Cafe> findByDateAndTimeAndDistance(final CreatePlanRequest request);

    List<Cafe> findByPlanIdAndMatchType(final Long planId,
                                        final PlanCafeMatchType matchType);

    List<Cafe> findNearestCafes(final BigDecimal latitude,
                                final BigDecimal longitude);

    List<Cafe> findDateAndLimit(final LocalDate date,
                                final Integer limit);
}
