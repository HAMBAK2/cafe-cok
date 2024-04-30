package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.member.dto.MyPagePlanDto;
import com.sideproject.hororok.member.dto.response.*;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.review.dto.MyPageReviewDto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.CombinationFixtures.조합_DTO;
import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_DTO;
import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_DTO_리스트;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.*;
import static com.sideproject.hororok.common.fixtures.PlanFixtures.계획;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.*;

public class MyPageFixtures {

    public static final Integer 페이지_번호 = 1;

    public static MyPageProfileResponse 마이페이지_프로필_응답() {
        return MyPageProfileResponse.of(사용자(), 리뷰_개수);
    }

    public static MyPagePlanDto 마이페이지_계획_DTO() {
        return MyPagePlanDto.of(계획(), 키워드_DTO());
    }

    public static MyPageReviewDto 마이페이지_리뷰_DTO() {
        return MyPageReviewDto.of(리뷰(), 리뷰_이미지_DTO_리스트(), 키워드_DTO_리스트());
    }

    public static List<MyPageReviewDto> 마이페이지_리뷰_DTO_리스트() {
        return Arrays.asList(마이페이지_리뷰_DTO());
    }

    public static MyPagePlansResponse 마이페이지_계획_리스트_응답() {
        return new MyPagePlansResponse(페이지_번호, Arrays.asList(마이페이지_계획_DTO()));
    }

    public static MyPageProfileEditResponse 마이페이지_프로필_수정_응답() {
        return new MyPageProfileEditResponse(맴버_닉네임, 멤버_기본_이미지);
    }

    public static MyPageReviewResponse 마이페이지_리뷰_리스트_응답() {
        return new MyPageReviewResponse(마이페이지_리뷰_DTO_리스트());
    }

    public static MyPageCombinationResponse 마이페이지_내조합_응답() {
        return MyPageCombinationResponse.from(Arrays.asList(조합_DTO()));
    }
}
