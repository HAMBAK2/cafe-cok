package com.sideproject.hororok.plan.domain;


import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.plan.domain.enums.MatchType;
import com.sideproject.hororok.plan.domain.enums.PlanCafeMatchType;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Table(name = "plan_cafes")
@Entity
public class PlanCafe extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plans_id")
    private Plan plan;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private PlanCafeMatchType matchType;

    protected PlanCafe() {
    }

    public PlanCafe(Plan plan, Cafe cafe, PlanCafeMatchType matchType) {
        this.plan = plan;
        this.cafe = cafe;
        this.matchType = matchType;
    }
}
