package com.sideproject.cafe_cok.review.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.review.domain.QReview;
import com.sideproject.cafe_cok.review.domain.Review;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sideproject.cafe_cok.review.domain.QReview.*;
import static org.springframework.util.StringUtils.isEmpty;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> findByCafeIdOrderByIdDesc(final Long cafeId,
                                                  final Long cursor,
                                                  final Pageable pageable) {

        return queryFactory
                .select(review)
                .from(review)
                .where(review.cafe.id.eq(cafeId),
                        reviewIdLt(cursor))
                .orderBy(review.id.desc())
                .fetch();
    }

    private BooleanExpression reviewIdLt(final Long cursor) {
        return isEmpty(cursor) ? null : review.id.lt(cursor);
    }
}
