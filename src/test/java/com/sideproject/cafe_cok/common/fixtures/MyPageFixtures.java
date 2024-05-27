package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.member.dto.response.*;
import com.sideproject.cafe_cok.review.dto.MyPageReviewDto;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.CombinationFixtures.조합_DTO;
import static com.sideproject.cafe_cok.common.fixtures.ImageFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.PlanFixtures.마이페이지_계획_DTO;
import static com.sideproject.cafe_cok.common.fixtures.ReviewFixtures.*;

public class MyPageFixtures {

    public static final Integer 페이지_번호 = 1;

    public static MyPageProfileResponse 마이페이지_프로필_응답() {
        return MyPageProfileResponse.of(사용자(), 리뷰_개수);
    }


    public static MyPageReviewDto 마이페이지_리뷰_DTO() {
        return MyPageReviewDto.of(리뷰(), Arrays.asList(리뷰_이미지()), Arrays.asList(키워드()));
    }

    public static List<MyPageReviewDto> 마이페이지_리뷰_DTO_리스트() {
        return Arrays.asList(마이페이지_리뷰_DTO());
    }

    public static MyPagePlansAllResponse 공유된_계획_전체_리스트() {
        return new MyPagePlansAllResponse(Arrays.asList(마이페이지_계획_DTO()));
    }
    public static MyPagePlansAllResponse 저장된_계획_전체_리스트() {
        return new MyPagePlansAllResponse(Arrays.asList(마이페이지_계획_DTO()));
    }

    public static MyPagePlansResponse 마이페이지_계획_리스트_응답() {
        return new MyPagePlansResponse(페이지_번호, Arrays.asList(마이페이지_계획_DTO()));
    }

    public static MyPageProfileEditResponse 마이페이지_프로필_수정_응답() {
        return MyPageProfileEditResponse.from(사용자());
    }

    public static MyPageReviewResponse 마이페이지_리뷰_리스트_응답() {
        return new MyPageReviewResponse(마이페이지_리뷰_DTO_리스트());
    }

    public static MyPageCombinationResponse 마이페이지_내조합_응답() {
        return new MyPageCombinationResponse(Arrays.asList(조합_DTO()));
    }


}
