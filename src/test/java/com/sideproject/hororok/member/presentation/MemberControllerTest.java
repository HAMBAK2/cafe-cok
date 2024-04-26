package com.sideproject.hororok.member.presentation;

import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;


import static com.sideproject.hororok.common.fixtures.MemberFixtures.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

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