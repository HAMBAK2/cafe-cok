package com.sideproject.hororok.member.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.member.dto.response.MyPageTagSaveResponse;
import com.sideproject.hororok.plan.domain.enums.PlanSortBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.hororok.common.fixtures.MyPageFixtures.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MyPageControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Test
    @DisplayName("마이페이지 상단의 프로필을 호출하는 API - 성공")
    public void test_myPage_profile_success() throws Exception{

        MyPageProfileResponse response = 마이페이지_프로필_응답();

        when(myPageService
                .profile(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/profile")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/profile/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("picture").description("사용자 프로필 사진 URL"),
                                fieldWithPath("reviewCount").description("리뷰 개수"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마이페이지 저장 탭을 눌렀을 때 동작 하는 API - 성공")
    public void test_myPage_tag_save_success() throws Exception {

        MyPageTagSaveResponse response = 마이페이지_태그_저장_응답();

        when(myPageService
                .tagSave(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/tag/save")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/tag/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("folderCount").description("폴더 개수"),
                                fieldWithPath("folders").description("폴더 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("folders[].folderId").description("폴더 ID"),
                                fieldWithPath("folders[].name").description("폴더 이름"),
                                fieldWithPath("folders[].color").description("폴더 색상"),
                                fieldWithPath("folders[].bookmarkCount").description("폴더 내 북마크 개수"),
                                fieldWithPath("folders[].visible").description("폴더 지도 노출 여부"),
                                fieldWithPath("folders[].defaultFolder").description("기본 폴더 여부"))))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("마이페이지 계획 탭의 저장한 계획(여정) 나타내는 API - 성공")
    public void test_saved_plan_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();

        when(myPageService.savedPlan(any(LoginMember.class), any(PlanSortBy.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/saved/plan?sortBy="+PlanSortBy.RECENT.name())
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/saved/plan/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        queryParameters(
                                parameterWithName("sortBy")
                                        .description("정렬 방식을 구분하는 필드\n\n" +
                                                "- RECENT: 최신 저장 순(Default)\n\n" + "- UPCOMING: 다가오는 여정 순")),
                        responseFields(
                                fieldWithPath("plans").type(JsonFieldType.ARRAY).description("계획(어정) 리스트"),
                                fieldWithPath("plans[].id").description("계획(여정)의 ID"),
                                fieldWithPath("plans[].keyword").type(JsonFieldType.OBJECT).description("계획(여정)의 대표 키워드"),
                                fieldWithPath("plans[].keyword.id").description("키워드 ID"),
                                fieldWithPath("plans[].keyword.category").description("키워드의 카테고리"),
                                fieldWithPath("plans[].keyword.name").description("키워드의 이름"),
                                fieldWithPath("plans[].location").description("사용자가 검색한 장소_필수X"),
                                fieldWithPath("plans[].visitDateTime").description("사용자가 검색한 날짜와 시간_필수X"))))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마이페이지 계획 탭의 공유한 계획(여정)을 나타내는 API - 성공")
    public void test_shared_plan_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();

        when(myPageService.sharedPlan(any(LoginMember.class), any(PlanSortBy.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/shared/plan?sortBy="+PlanSortBy.RECENT.name())
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/shared/plan/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        queryParameters(
                                parameterWithName("sortBy")
                                        .description("정렬 방식을 구분하는 필드\n\n" +
                                                "- RECENT: 최신 저장 순(Default)\n\n" + "- UPCOMING: 다가오는 여정 순")),
                        responseFields(
                                fieldWithPath("plans").type(JsonFieldType.ARRAY).description("계획(어정) 리스트"),
                                fieldWithPath("plans[].id").description("계획(여정)의 ID"),
                                fieldWithPath("plans[].keyword").type(JsonFieldType.OBJECT).description("계획(여정)의 대표 키워드"),
                                fieldWithPath("plans[].keyword.id").description("키워드 ID"),
                                fieldWithPath("plans[].keyword.category").description("키워드의 카테고리"),
                                fieldWithPath("plans[].keyword.name").description("키워드의 이름"),
                                fieldWithPath("plans[].location").description("사용자가 검색한 장소_필수X"),
                                fieldWithPath("plans[].visitDateTime").description("사용자가 검색한 날짜와 시간_필수X"))))
                .andExpect(status().isOk());
    }
}