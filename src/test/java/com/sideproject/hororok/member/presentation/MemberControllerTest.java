package com.sideproject.hororok.member.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;


import static com.sideproject.hororok.common.fixtures.MemberFixtures.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";


    @Test
    @DisplayName("마이 페이지 버튼을 클릭하면 MemberMyPageResponse와 200Ok를 반환한다.")
    public void test_my_page_success() throws Exception {

        MyPageResponse response = 마이페이지_조회_응답();
        when(memberService.myPage(any(LoginMember.class)))
                .thenReturn(response);


        mockMvc.perform(
                        get("/api/member/myPage")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member/myPage/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("nickname").description("유저 nickname"),
                                fieldWithPath("picture").description("유저 프로필 이미지 URL"),
                                fieldWithPath("reviewCount").description("리뷰의 총 개수"),
                                fieldWithPath("folderCount").description("북마크 폴더의 개수"),
                                fieldWithPath("folders").type(JsonFieldType.ARRAY).description("북마크 폴더의 정보를 담은 리스트"),
                                fieldWithPath("folders[].folderId").description("폴더의 ID"),
                                fieldWithPath("folders[].name").description("폴더의 이름"),
                                fieldWithPath("folders[].color").description("폴더 색상"),
                                fieldWithPath("folders[].bookmarkCount").description("폴더 내 북마크 개수"),
                                fieldWithPath("folders[].visible").description("지도 노출 여부"),
                                fieldWithPath("folders[].defaultFolder").description("기본 폴더 여부"))))
                .andExpect(status().isOk());
    }

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
    @DisplayName("마이페이지 계획텝에서 하나의 계획(여정)을 선택했을 때 동작")
    public void test_my_page_detail_success() throws Exception{


        MyPagePlanDetailResponse response = 마이페이지_계획_상세_응답();

        when(memberService.planDetail(any(Long.class)))
                .thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/member/myPage/plan/{planId}", response.getPlanId())
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member/myPage/plan/detail/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(
                                parameterWithName("planId").description("선택한 계획(여정)의 ID")),
                        responseFields(
                                fieldWithPath("planId").description("계획 ID"),
                                fieldWithPath("matchType").description("계획하기 결과 타입(MATCH, SIMILAR, MISMATCH)"),
                                fieldWithPath("locationName").description("검색한 위치 이름"),
                                fieldWithPath("minutes").description("도보 시간"),
                                fieldWithPath("visitDateTime").description("방문 날짜와 시간(월-일-시간-분"),
                                fieldWithPath("categoryKeywords").type(JsonFieldType.OBJECT).description("카테고리 별 키워드 정보"),
                                fieldWithPath("categoryKeywords.purpose").type(JsonFieldType.ARRAY).description("목적 카테고리 리스트(필수)"),
                                fieldWithPath("categoryKeywords.menu").type(JsonFieldType.ARRAY).description("메뉴 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.theme").type(JsonFieldType.ARRAY).description("테마 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.facility").type(JsonFieldType.ARRAY).description("시설 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.atmosphere").type(JsonFieldType.ARRAY).description("분위기 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("similarCafes").type(JsonFieldType.ARRAY)
                                        .description("유사한 카페(결과 타입이 MATCH, SIMILAR인 경우 존재, 아닌 경우 빈 리스트)"),
                                fieldWithPath("similarCafes[].id").description("카페 ID"),
                                fieldWithPath("similarCafes[].name").description("카페 이름"),
                                fieldWithPath("similarCafes[].phoneNumber").description("전화번호"),
                                fieldWithPath("similarCafes[].roadAddress").description("도로명 주소"),
                                fieldWithPath("similarCafes[].longitude").description("경도"),
                                fieldWithPath("similarCafes[].latitude").description("위도"),
                                fieldWithPath("similarCafes[].starRating").description("별점"),
                                fieldWithPath("similarCafes[].reviewCount").description("리뷰 수"),
                                fieldWithPath("similarCafes[].image").description("카페 이미지 URL"),
                                fieldWithPath("matchCafes").type(JsonFieldType.ARRAY)
                                        .description("일치하는 카페(결과 타입이 MATCH인 경우 존재, 아닌 경우 빈 리스트, 형식은 유사한 카페와 동일)"))))
                .andExpect(status().isOk());


    }

}