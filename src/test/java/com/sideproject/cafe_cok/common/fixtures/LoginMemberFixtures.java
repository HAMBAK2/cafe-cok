package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.auth.dto.LoginMember;

public class LoginMemberFixtures {



    /* 로그인 맴버 */
    public static final Long 로그인_맴버_아이디 = 1L;


    public static LoginMember 로그인_맴버() {
        return new LoginMember(로그인_맴버_아이디);
    }
}
