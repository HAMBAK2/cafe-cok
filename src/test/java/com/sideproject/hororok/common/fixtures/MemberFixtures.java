package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.SocialType;

public class MemberFixtures {

    /* 맴버 */
    public static final String 맴버_이메일 = "cafe_cok_@gmail.com";
    public static final String 맴버_닉네임 = "cafe_cok_nickname";

    public static Member 사용자() {
        return new Member(맴버_이메일, 맴버_닉네임, SocialType.KAKAO);
    }
}
