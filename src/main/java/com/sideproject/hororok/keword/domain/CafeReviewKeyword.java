package com.sideproject.hororok.keword.domain;


import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.review.domain.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cafe_review_keywords")
public class CafeReviewKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywords_id")
    private Keyword keyword;


}
