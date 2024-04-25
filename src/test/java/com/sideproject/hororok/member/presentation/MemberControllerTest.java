package com.sideproject.hororok.member.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;


import static com.sideproject.hororok.common.fixtures.MemberFixtures.마이페이지_계획_응답;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.마이페이지_조회_응답;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Test
    @DisplayName("마이페이지의 계획 탭을 눌렀을 때 동작 - 성공")
    public void test_my_page_plan_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();
        when(memberService
                .plan(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/member/myPage/plan")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member/myPage/plan/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("savedPlans").type(JsonFieldType.ARRAY)
                                        .description("저장한 여정의 리스트(최신 저장순으로 정렬 최대 5개)"),
                                fieldWithPath("savedPlans[].id").description("계획(여정) ID"),
                                fieldWithPath("savedPlans[].purpose").description("목적 카테고리의 키워드"),
                                fieldWithPath("savedPlans[].location").description("장소"),
                                fieldWithPath("savedPlans[].visitDateTime").description("방문일"),
                                fieldWithPath("sharedPlans").type(JsonFieldType.ARRAY)
                                        .description("공유한 여정의 리스트(최신 저장순으로 정렬 최대 5개)"),
                                fieldWithPath("sharedPlans[].id").description("계획(여정) ID"),
                                fieldWithPath("sharedPlans[].purpose").description("목적 카테고리의 키워드"),
                                fieldWithPath("sharedPlans[].location").description("장소"),
                                fieldWithPath("sharedPlans[].visitDateTime").description("방문일"))))
                .andExpect(status().isOk());
    }




    @Test
    @DisplayName("마이 페이지 버튼을 클릭하면 MemberMyPageResponse와 200Ok를 반환한다.")
    public void test_my_page() throws Exception {

        MyPageResponse response = 마이페이지_조회_응답();
        when(memberService.myPage(any(LoginMember.class)))
                .thenReturn(response);


        mockMvc.perform(
                        get("/api/member/myPage")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andExpect(jsonPath("$.folderCount").value(response.getFolderCount()))
                .andExpect(jsonPath("$.reviewCount").value(response.getReviewCount()))
                .andExpect(jsonPath("$.picture").value(response.getPicture()))
                .andExpect(jsonPath("$.folders", hasSize(response.getFolders().size())));
    }

}