package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.member.dto.MyPagePlanDto;
import com.sideproject.hororok.member.dto.response.*;

import java.util.Arrays;

import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_DTO;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.*;
import static com.sideproject.hororok.common.fixtures.PlanFixtures.계획;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_개수;

public class MyPageFixtures {

    public static final Integer 페이지_번호 = 1;

    public static MyPageProfileResponse 마이페이지_프로필_응답() {
        return MyPageProfileResponse.of(사용자(), 리뷰_개수);
    }

    public static MyPagePlanDto 마이페이지_계획_DTO() {
        return MyPagePlanDto.of(계획(), 키워드_DTO());
    }

    public static MyPagePlanResponse 마이페이지_계획_응답() {
        return new MyPagePlanResponse(Arrays.asList(마이페이지_계획_DTO()));
    }

    public static MyPagePlansResponse 마이페이지_계획_리스트_응답() {
        return new MyPagePlansResponse(페이지_번호, Arrays.asList(마이페이지_계획_DTO()));
    }

    public static MyPageProfileEditResponse 마이페이지_프로필_수정_응답() {
        return new MyPageProfileEditResponse(맴버_닉네임, 멤버_기본_이미지);
    }




}
