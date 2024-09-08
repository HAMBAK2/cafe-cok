package com.sideproject.cafe_cok.member.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

import static com.sideproject.cafe_cok.member.domain.QMember.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public void update(final Long memberId,
                       final String nickname,
                       final String picture) {

        queryFactory.update(member)
                .set(member.nickname, nickname)
                .set(member.picture, picture)
                .where(member.id.eq(memberId))
                .execute();
    }

    @Override
    public void update(final Long memberId,
                       final LocalDateTime deletedAt) {

        queryFactory.update(member)
                .set(member.deletedAt, deletedAt)
                .where(member.id.eq(memberId))
                .execute();
    }
}
