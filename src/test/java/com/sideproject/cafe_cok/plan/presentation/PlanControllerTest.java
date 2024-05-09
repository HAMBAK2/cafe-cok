package com.sideproject.cafe_cok.plan.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.common.annotation.ControllerTest;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SavePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SharePlanRequest;
import com.sideproject.cafe_cok.plan.dto.response.CreatePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.DeletePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.SharePlanResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.cafe_cok.common.fixtures.PlanFixtures.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlanControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer JWT TOKEN";

    @Test
    @DisplayName("계획하기 저장 - 성공")
    public void test_plan_save_success() throws Exception {

        SavePlanRequest request = 계획_저장_요청();
        SavePlanResponse response = 계획_저장_응답();

        when(planService
                .save(any(SavePlanRequest.class), any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        patch("/api/plan/save")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("plan/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        requestFields(
                                fieldWithPath("planId").description("저장할 계획 ID")),
                        responseFields(
                                fieldWithPath("planId").description("저장한 계획 ID"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(response.getPlanId()));
    }

    @Test
    @DisplayName("계획하기 공유 - 성공")
    public void test_plan_share_success() throws Exception {

        SharePlanRequest request = 계획_공유_요청();
        SharePlanResponse response = 계획_공유_응답();

        when(planService
                .share(any(SharePlanRequest.class), any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        patch("/api/plan/share")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("plan/share/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        requestFields(
                                fieldWithPath("planId").description("공유할 계획 ID")),
                        responseFields(
                                fieldWithPath("planId").description("공유한 계획 ID"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(response.getPlanId()));
    }

    @Test
    @DisplayName("계획하기 요청 - 성공")
    public void test_plan_success() throws Exception {

        CreatePlanRequest request = 계획_요청();
        CreatePlanResponse response = 계획_응답();

        when(planService
                .plan(any(CreatePlanRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/plan")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("plan/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("locationName").description("검색한 위치 이름"),
                                fieldWithPath("latitude").description("검색한 위치 위도"),
                                fieldWithPath("longitude").description("검색한 위치 경"),
                                fieldWithPath("minutes").description("도보 시간"),
                                fieldWithPath("date").description("방문 날"),
                                fieldWithPath("startTime").description("방문 시작 시간"),
                                fieldWithPath("endTime").description("방문 끝 시간"),
                                fieldWithPath("keywords").description("키워드 이름 리스트").type(JsonFieldType.ARRAY)),
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
                                fieldWithPath("recommendCafes").type(JsonFieldType.ARRAY)
                                        .description("추천 카페(결과 티입이 MISMATCH 경우 존재, 아닌 경우 빈 리스트)"),
                                fieldWithPath("recommendCafes[].id").description("카페 ID"),
                                fieldWithPath("recommendCafes[].bookmarks").type(JsonFieldType.ARRAY).description("북마크 리스트"),
                                fieldWithPath("recommendCafes[].bookmarks[].bookmarkId").description("북마크 ID"),
                                fieldWithPath("recommendCafes[].bookmarks[].folderId").description("북마크 폴더  ID"),
                                fieldWithPath("recommendCafes[].name").description("카페 이름"),
                                fieldWithPath("recommendCafes[].phoneNumber").description("전화번호"),
                                fieldWithPath("recommendCafes[].roadAddress").description("도로명 주소"),
                                fieldWithPath("recommendCafes[].longitude").description("경도"),
                                fieldWithPath("recommendCafes[].latitude").description("위도"),
                                fieldWithPath("recommendCafes[].starRating").description("별점"),
                                fieldWithPath("recommendCafes[].reviewCount").description("리뷰 수"),
                                fieldWithPath("recommendCafes[].imageUrl").description("카페 이미지 URL"),
                                fieldWithPath("matchCafes").type(JsonFieldType.ARRAY)
                                        .description("일치하는 카페(결과 타입이 MATCH인 경우 존재, 아닌 경우 빈 리스트, 형식은 추천 카페와 동일)"),
                                fieldWithPath("similarCafes").type(JsonFieldType.ARRAY)
                                        .description("유사한 카페(결과 타입이 MATCH, SIMILAR인 경우 존재, 아닌 경우 빈 리스트, 형식은 추천 카페와 동일)"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계획(여정)을 삭제하는 기능")
    public void test_plan_delete_success() throws Exception {

        Long planId = 계획_ID;
        DeletePlanResponse response = 계획_삭제_응답();

        when(planService
                .delete(any(PlanStatus.class), any(Long.class)))
                .thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .delete("/api/plan/{planId}/delete?status="+PlanStatus.SAVED.name(), planId)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("plan/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(parameterWithName("planId").description("삭제할 계획의 ID")),
                        queryParameters(parameterWithName("status").description("삭제할 계획의 상태 \n\n" +
                                "- SAVED: 저장된 계획\n\n" + "- SHARED: 공유된 계획")),
                        responseFields(
                                fieldWithPath("planId").description("삭제한 계획의 ID"))))
                .andExpect(status().isOk());

    }




}