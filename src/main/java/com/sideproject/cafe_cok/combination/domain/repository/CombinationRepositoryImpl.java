package com.sideproject.cafe_cok.combination.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.combination.dto.QCombinationDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sideproject.cafe_cok.combination.domain.QCombination.*;

public class CombinationRepositoryImpl implements CombinationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CombinationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CombinationDto> findByMemberId(final Long memberId) {

        return queryFactory
                .select(new QCombinationDto(
                        combination.id,
                        combination.name,
                        combination.icon))
                .from(combination)
                .where(combination.member.id.eq(memberId))
                .fetch();
    }

}
