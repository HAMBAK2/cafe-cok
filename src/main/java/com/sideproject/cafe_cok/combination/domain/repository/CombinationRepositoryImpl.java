package com.sideproject.cafe_cok.combination.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.combination.dto.QCombinationDto;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import static com.sideproject.cafe_cok.combination.domain.QCombination.*;

public class CombinationRepositoryImpl implements CombinationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CombinationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public void update(final CombinationRequest request) {

        queryFactory.update(combination)
                .set(combination.name, request.getName())
                .set(combination.icon, request.getIcon())
                .execute();
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
