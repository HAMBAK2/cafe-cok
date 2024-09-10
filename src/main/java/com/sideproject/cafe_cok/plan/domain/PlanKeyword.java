package com.sideproject.cafe_cok.plan.domain;


import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "plan_keywords")
public class PlanKeyword extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plans_id")
    private Plan plan;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "keywords_id")
    private Keyword keyword;

    @Builder
    public PlanKeyword(final Long id,
                       final Plan plan,
                       final Keyword keyword) {

        if(plan != null) changePlan(plan);
        this.keyword = keyword;
        this.id = id;
    }

    private void changePlan(final Plan plan) {
        this.plan = plan;
        plan.getPlanKeywords().add(this);
    }
}
