package com.sideproject.cafe_cok.keword.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import jakarta.persistence.EntityManager;

import java.util.List;

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

    private BooleanExpression combinationIdEq(final Long combinationId) {
        return isEmpty(combinationId) ? null : combinationKeyword.combination.id.eq(combinationId);
    }
}
