package com.sideproject.cafe_cok.plan.domain.repository;

import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.plan.domain.PlanKeyword;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanKeywordException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanKeywordRepository extends JpaRepository<PlanKeyword, Long> {

    List<PlanKeyword> findByPlanId(final Long planId);

    void deleteByPlanId(final Long planId);
}
