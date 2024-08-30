package com.sideproject.cafe_cok.auth.domain;


import com.sideproject.cafe_cok.auth.exception.NoSuchOAuthTokenException;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class OAuthTokenRepositoryTest {

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("memberId 기반 OAuthToken이 존재하는지 조회한다.")
    void exists_by_member_id() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        OAuthToken oAuthToken = new OAuthToken(savedMember, REFRESH_TOKEN);
        OAuthToken savedOAuthToken = oAuthTokenRepository.save(oAuthToken);

        //when
        boolean isExist1 = oAuthTokenRepository.existsByMemberId(savedMember.getId());
        boolean isExist2 = oAuthTokenRepository.existsByMemberId(NON_EXISTENT_ID);

        //then
        assertThat(isExist1).isTrue();
        assertThat(isExist2).isFalse();
    }

    @Test
    @DisplayName("memberId 기반 OAuthToken 조회")
    void get_by_member_id() {

        //given
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        OAuthToken oAuthToken = new OAuthToken(savedMember, REFRESH_TOKEN);
        OAuthToken savedOAuthToken = oAuthTokenRepository.save(oAuthToken);

        //when
        OAuthToken findOAuthToken = oAuthTokenRepository.getByMemberId(savedMember.getId());

        //then
        assertThat(findOAuthToken).isEqualTo(savedOAuthToken);
        assertThat(findOAuthToken.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
    }

    @Test
    @DisplayName("존재하지 않는 memberId로 조회 시 에러 반환")
    void get_by_non_existent_member_id() {

        //when & then
        assertThatExceptionOfType(NoSuchOAuthTokenException.class)
                .isThrownBy(() -> oAuthTokenRepository.getByMemberId(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 OAuthToken 이 존재하지 않습니다.");
    }

}