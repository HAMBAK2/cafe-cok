package com.sideproject.hororok.plan.domain.repository;

import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.exception.NoSuchPlanException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    default Optional<Plan> findMatchingPlan(final Plan plan) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "isSaved", "isShared")
                .withIgnoreCase();

        Example<Plan> example = Example.of(plan, matcher);
        return findOne(example);
    }

    default Plan getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchPlanException::new);
    }
}
