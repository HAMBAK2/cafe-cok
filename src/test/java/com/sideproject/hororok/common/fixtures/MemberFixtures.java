package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.enums.SocialType;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageResponse;

import java.util.Arrays;

import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.일반_폴더_DTO;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페_DTO_리스트;
import static com.sideproject.hororok.common.fixtures.KeywordFixtures.카테고리_키워드_DTO;
import static com.sideproject.hororok.common.fixtures.PlanFixtures.계획;
import static com.sideproject.hororok.common.fixtures.PlanFixtures.계획_DTO;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_개수;


public class MemberFixtures {

    /* 맴버 */
    public static final Long 맴버_ID = 1L;
    public static final String 맴버_이메일 = "cafe_cok_@gmail.com";
    public static final String 맴버_닉네임 = "cafe_cok_nickname";
    public static final String 멤버_기본_이미지 =
            "https://kr.object.ncloudstorage.com/hororok-bucket/member/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-20%20%EC%98%A4%ED%9B%84%2011.46.07.png";

    public static Member 사용자() {
        return new Member(맴버_이메일, 맴버_닉네임, 멤버_기본_이미지, SocialType.KAKAO);
    }

    public static MyPageResponse 마이페이지_조회_응답() {
        return new MyPageResponse(사용자(), 리뷰_개수, Arrays.asList(일반_폴더_DTO()));
    }

    public static MyPagePlanResponse 마이페이지_계획_응답() {
        return MyPagePlanResponse.from(Arrays.asList(계획_DTO()), Arrays.asList(계획_DTO()));
    }

    public static MyPagePlanDetailResponse 마이페이지_계획_상세_응답() {
        return MyPagePlanDetailResponse.of(계획(), 카테고리_키워드_DTO(), 카페_DTO_리스트());
    }


}
