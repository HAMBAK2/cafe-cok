package com.sideproject.cafe_cok.member.domain.repository;

import com.sideproject.cafe_cok.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMembersInactiveForThreeMonths(final LocalDateTime threeMonthsAgo);
}
