package com.sideproject.cafe_cok.member.domain.repository;

import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmailAndDeletedAtIsNull(final String email);

    Optional<Member> findByEmailAndDeletedAtIsNotNull(final String email);

    boolean existsByEmailAndDeletedAtIsNull(final String email);

    boolean existsByEmailAndDeletedAtIsNotNull(final String email);

    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchMemberException::new);
    }

    default Member getByEmailAndDeletedAtIsNull(final String email) {
        return findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(NoSuchMemberException::new);
    }

    default Member getByEmailAndDeletedAtIsNotNull(final String email) {
        return findByEmailAndDeletedAtIsNotNull(email)
                .orElseThrow(NoSuchMemberException::new);
    }


}
