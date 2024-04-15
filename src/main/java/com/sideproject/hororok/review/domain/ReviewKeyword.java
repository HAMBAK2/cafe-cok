package com.sideproject.hororok.review.domain;

import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.keword.domain.Keyword;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "review_keywords")
public class ReviewKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;
}
