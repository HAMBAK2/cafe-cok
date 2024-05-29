package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;

import java.math.BigDecimal;
import java.util.List;

public interface CafeRepositoryCustom {

    List<Cafe> findNotMismatchCafes(final CreatePlanRequest request);

    List<Cafe> findWithinRadiusCafeList(final BigDecimal latitude,
                                        final BigDecimal longitude);

    List<Cafe> findByPlanIdAndMatchType(final Long planId,
                                        final PlanCafeMatchType matchType);
}
