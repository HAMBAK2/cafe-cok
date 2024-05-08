package com.sideproject.hororok.plan.domain.repository;

import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.exception.NoSuchPlanException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByMemberId(final Long memberId);

    Page<Plan> findPageByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT p "+
            "FROM Plan p " +
            "WHERE p.member.id = :memberId " +
                "AND (p.visitDate >= :visitDate " +
                    "AND (p.visitStartTime >= :visitStartTime OR p.visitStartTime IS NULL))")
    Page<Plan> findPageByMemberIdAndUpcomingPlanCondition(
            Long memberId, LocalDate visitDate, LocalTime visitStartTime, Pageable pageable);

    default List<Plan> findMatchingPlan(final Plan plan) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "isSaved", "isShared", "createdDate", "lastModifiedDate")
                .withIncludeNullValues()
                .withIgnoreCase();

        Example<Plan> example = Example.of(plan, matcher);
        return findAll(example);
    }

    default Plan getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchPlanException::new);
    }


}
