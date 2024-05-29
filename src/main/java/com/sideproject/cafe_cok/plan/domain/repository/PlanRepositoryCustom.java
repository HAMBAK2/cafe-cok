package com.sideproject.cafe_cok.plan.domain.repository;

import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PlanRepositoryCustom {

    List<PlanKeywordDto> findPlansByMemberIdAndCategory(final Long memberId,
                                                        final Category category,
                                                        final PlanSortBy planSortBy,
                                                        final PlanStatus status,
                                                        final Pageable pageable);
}
