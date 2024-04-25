package com.sideproject.hororok.plan.domain;


import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.keword.domain.Keyword;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Table(name = "plan_keywords")
@Entity
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

    protected PlanKeyword() {
    }

    public PlanKeyword(final Plan plan, final Keyword keyword) {
        this.plan = plan;
        this.keyword = keyword;
    }
}
