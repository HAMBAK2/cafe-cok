package com.sideproject.cafe_cok.combination.domain;

import com.sideproject.cafe_cok.keword.domain.Keyword;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "combination_keywords")
public class CombinationKeyword {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "combinations_id")
    private Combination combination;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "keywords_id")
    private Keyword keyword;

    protected CombinationKeyword() {
    }

    public CombinationKeyword(final Combination combination, final Keyword keyword) {
        this.combination = combination;
        this.keyword = keyword;
    }
}
