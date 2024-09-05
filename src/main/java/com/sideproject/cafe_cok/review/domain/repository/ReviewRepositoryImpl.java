package com.sideproject.cafe_cok.review.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.util.QuerydslUtil;
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
    public List<Review> findByCafeId(final Long cafeId,
                                     final Long cursor,
                                     final Pageable pageable) {

        NumberPath<Long> idPath = review.id;
        List<OrderSpecifier<?>> orderSpecifiers = QuerydslUtil.getOrderSpecifiers(pageable, idPath);

        return queryFactory
                .select(review)
                .from(review)
                .where(review.cafe.id.eq(cafeId),
                        reviewIdLt(cursor),
                        memberDeletedAtIsNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Review> findByCafeId(final Long cafeId,
                                     final Pageable pageable) {

        NumberPath<Long> idPath = review.id;
        List<OrderSpecifier<?>> orderSpecifiers = QuerydslUtil.getOrderSpecifiers(pageable, idPath);

        return queryFactory
                .select(review)
                .from(review)
                .where(review.cafe.id.eq(cafeId),
                        memberDeletedAtIsNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression reviewIdLt(final Long cursor) {
        return isEmpty(cursor) ? null : review.id.lt(cursor);
    }

    private BooleanExpression memberDeletedAtIsNull() {
        return review.member.deletedAt.isNull();
    }
}
