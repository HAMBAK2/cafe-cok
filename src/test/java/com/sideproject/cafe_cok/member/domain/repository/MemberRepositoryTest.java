package com.sideproject.cafe_cok.member.domain.repository;


import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("email 기반으로 deleateAt 컬럼이 Null인 member를 조회한다.")
    void find_by_email_and_deleted_at_is_null() {

        //given
        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        Optional<Member> findOptionalMember = memberRepository.findByEmailAndDeletedAtIsNull(savedMember.getEmail());

        //then
        assertThat(findOptionalMember).isPresent();
        assertThat(findOptionalMember.get()).isEqualTo(savedMember);
    }

    @Test
    @DisplayName("email 기반으로 deleateAt 컬럼이 Null이 아닌 member를 조회한다.")
    void find_by_email_and_deleted_at_is_not_null() {

        //given
        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .deletedAt(LocalDateTime.now())
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        Optional<Member> findOptionalMember = memberRepository.findByEmailAndDeletedAtIsNotNull(savedMember.getEmail());

        //then
        assertThat(findOptionalMember).isPresent();
        assertThat(findOptionalMember.get()).isEqualTo(savedMember);
    }

    @Test
    @DisplayName("nickname 기반으로 member가 존재하는지 확인한다.")
    void exists_by_nickname() {

        //given
        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        boolean isExist = memberRepository.existsByNickname(MEMBER_NICKNAME);
        boolean isExist2 = memberRepository.existsByNickname(MEMBER_NOT_EXIST_NICKNAME);

        //then
        assertThat(isExist).isTrue();
        assertThat(isExist2).isFalse();
    }

    @Test
    @DisplayName("id 기반으로 member를 조회한다.")
    void get_by_id() {

        //given
        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.getById(savedMember.getId());

        //then
        assertThat(findMember).isEqualTo(savedMember);
        assertThat(findMember.getEmail()).isEqualTo(MEMBER_EMAIL);
        assertThat(findMember.getNickname()).isEqualTo(MEMBER_NICKNAME);
        assertThat(findMember.getSocialType()).isEqualTo(MEMBER_SOCIAL_TYPE);
    }

    @Test
    @DisplayName("id 기반으로 member 조회 시 존재하지 않으면 에러를 반환한다.")
    void get_by_non_existent_id() {

        //when & then
        assertThatExceptionOfType(NoSuchMemberException.class)
                .isThrownBy(() -> memberRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 회원이 존재하지 않습니다.");
    }
}