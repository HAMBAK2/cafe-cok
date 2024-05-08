package com.sideproject.hororok.member.domain.repository;

import com.sideproject.hororok.member.domain.WithdrawnMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawnMemberRepository extends JpaRepository<WithdrawnMember, Long> {
}
