package com.sideproject.cafe_cok.member.domain.repository;

import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.common.annotation.RepositoryTest;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("맴버 ID로 맴버를 조회한다.")
    public void test_get_member_by_id() {

        Member member = 사용자();
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.getById(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("존재하지 않는 member Id로 조회시 에러를 리턴한다.")
    public void test_return_error_not_exist_member_id() {

        Long notExistId = 3L;

        assertThatThrownBy(() -> memberRepository.getById(notExistId))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @Test
    @DisplayName("맴버 email로 맴버를 조회한다.")
    public void test_get_member_by_email() {

        Member member = 사용자();
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.getByEmail(savedMember.getEmail());

        assertThat(findMember.getEmail()).isEqualTo(savedMember.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 member email로 조회시 에러를 리턴한다.")
    public void test_return_error_not_exist_member_email() {

        String notExistEmail = "not@eixst.com";

        assertThatThrownBy(() -> memberRepository.getByEmail(notExistEmail))
                .isInstanceOf(NoSuchMemberException.class);
    }
}