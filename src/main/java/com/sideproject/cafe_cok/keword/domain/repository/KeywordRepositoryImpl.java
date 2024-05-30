package com.sideproject.cafe_cok.keword.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.QKeyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.keword.dto.QKeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.QKeywordDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
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
    public List<String> findNamesByReviewIdAndCategory(final Long reviewId,
                                                       final Category category,
                                                       final Pageable pageable) {

        return queryFactory
                .select(keyword.name)
                .from(keyword)
                .leftJoin(cafeReviewKeyword).on(cafeReviewKeyword.id.eq(keyword.id))
                .where(cafeReviewKeyword.review.id.eq(reviewId),
                        categoryEq(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<String> findNamesByCafeId(final Long cafeId) {
        return queryFactory
                .select(keyword.name)
                .distinct()
                .from(cafeReviewKeyword)
                .leftJoin(cafeReviewKeyword.keyword, keyword)
                .where(cafeIdEq(cafeId))
                .fetch();
    }

    @Override
    public List<KeywordDto> findByReviewIdAndCategory(final Long reviewId,
                                                      final Category category) {
        return queryFactory
                .select(new QKeywordDto(keyword))
                .from(cafeReviewKeyword)
                .leftJoin(cafeReviewKeyword.keyword, keyword)
                .where(reviewIdEq(reviewId),
                        categoryEq(category))
                .fetch();
    }

    @Override
    public List<KeywordDto> findKeywordDtoListByCafeIdOrderByCountDesc(final Long cafeId,
                                                                       final Pageable pageable) {

        return queryFactory
                .select(new QKeywordDto(keyword))
                .from(keyword)
                .leftJoin(cafeReviewKeyword).on(keyword.id.eq(cafeReviewKeyword.keyword.id))
                .where(cafeIdEq(cafeId),
                        cafeReviewKeyword.review.deletedAt.isNull())
                .groupBy(keyword.id)
                .orderBy(keyword.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<KeywordCountDto> findKeywordCountDtoListByCafeIdOrderByCountDesc(final Long cafeId,
                                                                                 final Pageable pageable) {

        return queryFactory
                .select(new QKeywordCountDto(
                        keyword.name,
                        keyword.id.count()
                ))
                .from(keyword)
                .leftJoin(cafeReviewKeyword).on(keyword.id.eq(cafeReviewKeyword.keyword.id))
                .where(cafeIdEq(cafeId))
                .groupBy(keyword.id)
                .orderBy(keyword.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression cafeIdEq(final Long cafeId) {
        return isEmpty(cafeId) ? null : cafeReviewKeyword.cafe.id.eq(cafeId);
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
