package com.sideproject.cafe_cok.keword.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sideproject.cafe_cok.keword.domain.QCafeReviewKeyword.*;
import static org.springframework.util.StringUtils.isEmpty;
import static com.sideproject.cafe_cok.combination.domain.QCombinationKeyword.*;
import static com.sideproject.cafe_cok.keword.domain.QKeyword.*;

public class KeywordRepositoryImpl implements KeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public KeywordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Keyword> findByCombinationId(final Long combinationId) {
        return queryFactory
                .select(keyword)
                .from(combinationKeyword)
                .leftJoin(combinationKeyword.keyword, keyword)
                .where(combinationIdEq(combinationId))
                .fetch();
    }

    @Override
    public List<String> findNamesByCombinationId(final Long combinationId) {
        return queryFactory
                .select(keyword.name)
                .from(combinationKeyword)
                .leftJoin(combinationKeyword.keyword, keyword)
                .where(combinationIdEq(combinationId))
                .fetch();
    }

    @Override
    public List<Keyword> findByReviewIdAndCategory(final Long reviewId, final Category category) {
        return queryFactory
                .select(keyword)
                .from(cafeReviewKeyword)
                .leftJoin(cafeReviewKeyword.keyword, keyword)
                .where(reviewIdEq(reviewId),
                        categoryEq(category))
                .fetch();
    }

    private BooleanExpression combinationIdEq(final Long combinationId) {
        return isEmpty(combinationId) ? null : combinationKeyword.combination.id.eq(combinationId);
    }

    private BooleanExpression reviewIdEq(final Long reviewId) {
        return isEmpty(reviewId) ? null : cafeReviewKeyword.review.id.eq(reviewId);
    }

    private BooleanExpression categoryEq(final Category category) {
        return isEmpty(category) ? null : keyword.category.eq(category);
    }
}
