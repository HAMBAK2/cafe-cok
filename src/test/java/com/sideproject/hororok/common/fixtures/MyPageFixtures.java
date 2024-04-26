package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.member.dto.response.MyPageTagSaveResponse;

import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_개수;

public class MyPageFixtures {


    public static MyPageProfileResponse 마이페이지_프로필_응답() {
        return MyPageProfileResponse.of(사용자(), 리뷰_개수);
    }

    public static MyPageTagSaveResponse 마이페이지_태그_저장_응답() {
        return MyPageTagSaveResponse.of(폴더_리스트_사이즈, 일반_폴더_DTO_리스트());
    }




}
