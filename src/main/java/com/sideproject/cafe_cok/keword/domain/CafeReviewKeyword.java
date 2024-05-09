package com.sideproject.cafe_cok.keword.domain;


import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.review.domain.Review;
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

    protected CafeReviewKeyword() {
    }

    public CafeReviewKeyword(final Cafe cafe, final Review review, final Keyword keyword) {
        this.cafe = cafe;
        this.review = review;
        this.keyword = keyword;
    }
}
