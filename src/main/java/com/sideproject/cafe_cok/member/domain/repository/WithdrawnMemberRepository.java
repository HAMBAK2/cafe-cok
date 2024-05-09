package com.sideproject.cafe_cok.member.domain.repository;

import com.sideproject.cafe_cok.member.domain.WithdrawnMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawnMemberRepository extends JpaRepository<WithdrawnMember, Long> {
}
