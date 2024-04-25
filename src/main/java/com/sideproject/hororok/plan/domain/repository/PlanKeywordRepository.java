package com.sideproject.hororok.plan.domain.repository;

import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.plan.domain.PlanKeyword;
import com.sideproject.hororok.plan.exception.NoSuchPlanKeywordException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanKeywordRepository extends JpaRepository<PlanKeyword, Long> {

    List<PlanKeyword> findByPlanId(Long planId);

    void deleteByPlanId(Long planId);

    Optional<PlanKeyword> findFirstByPlanIdAndKeywordCategory(Long planId, Category category);

    default PlanKeyword getFirstByPlanIdAndKeywordCategory(Long planId, Category category) {
        return findFirstByPlanIdAndKeywordCategory(planId, category)
                .orElseThrow(NoSuchPlanKeywordException::new);
    }

}
