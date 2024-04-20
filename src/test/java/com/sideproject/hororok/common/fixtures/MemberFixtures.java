package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.SocialType;


public class MemberFixtures {

    /* 맴버 */
    public static final String 맴버_이메일 = "cafe_cok_@gmail.com";
    public static final String 맴버_닉네임 = "cafe_cok_nickname";

    public static final String 멤버_기본_이미지 =
            "https://kr.object.ncloudstorage.com/hororok-bucket/member/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-20%20%EC%98%A4%ED%9B%84%2011.46.07.png";

    public static Member 사용자() {
        return new Member(맴버_이메일, 맴버_닉네임, 멤버_기본_이미지, SocialType.KAKAO);
    }
}
