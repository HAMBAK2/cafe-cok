package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;

import java.util.List;

public interface CafeRepositoryCustom {

    List<Cafe> findNotMismatchCafes(final CreatePlanRequest request);
}
