package com.sideproject.cafe_cok.member.domain.repository;

import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmailAndDeletedAtIsNull(final String email);

    Optional<Member> findByEmailAndDeletedAtIsNotNull(final String email);

    boolean existsByNickname(final String nickname);

    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new NoSuchMemberException("[ID : " + id + "] 에 해당하는 회원이 존재하지 않습니다."));
    }
}
