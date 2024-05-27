package com.sideproject.cafe_cok.plan.domain.repository;

import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.domain.PlanCafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanCafeRepository extends JpaRepository<PlanCafe, Long> {


    void deleteByPlanId(Long planId);

    List<PlanCafe> findByPlanIdAndMatchType(Long planId, PlanCafeMatchType matchType);
}
