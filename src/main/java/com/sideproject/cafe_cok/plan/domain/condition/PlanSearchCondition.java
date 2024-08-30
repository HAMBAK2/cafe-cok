package com.sideproject.cafe_cok.plan.domain.condition;

import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import lombok.Getter;

@Getter
public class PlanSearchCondition {

    private Long memberId;
    private Category category;
    private PlanSortBy planSortBy;
    private PlanStatus status;

    public PlanSearchCondition(final Long memberId,
                               final Category category,
                               final PlanSortBy planSortBy,
                               final PlanStatus status) {
        this.memberId = memberId;
        this.category = category;
        this.planSortBy = planSortBy;
        this.status = status;
    }
}
