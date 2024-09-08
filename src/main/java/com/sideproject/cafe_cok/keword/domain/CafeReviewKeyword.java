package com.sideproject.cafe_cok.keword.domain;


import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public CafeReviewKeyword(final Long id,
                             final Cafe cafe,
                             final Review review,
                             final Keyword keyword) {
        this.id = id;
        this.keyword = keyword;
        if(cafe != null) changeCafe(cafe);
        if(review != null) changeReview(review);
    }

    public void changeReview(final Review review) {
        this.review = review;
        review.getCafeReviewKeywords().add(this);
    }

    public void changeCafe(final Cafe cafe) {
        this.cafe = cafe;
        cafe.getCafeReviewKeywords().add(this);
    }
}
