package com.sideproject.cafe_cok.member.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.QMember;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.sideproject.cafe_cok.member.domain.QMember.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Member> findMembersInactiveForThreeMonths(final LocalDateTime threeMonthsAgo) {
        return queryFactory
                .selectFrom(member)
                .where(member.deletedAt.loe(threeMonthsAgo))
                .fetch();
    }
}
