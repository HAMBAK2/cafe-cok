package com.sideproject.hororok.combination.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.combination.dto.request.CombinationCreateRequest;
import com.sideproject.hororok.combination.dto.response.CombinationCreateResponse;
import com.sideproject.hororok.combination.dto.response.CombinationDetailResponse;
import com.sideproject.hororok.common.annotation.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.hororok.common.fixtures.CombinationFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CombinationControllerTest extends ControllerTest {


    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Test
    @DisplayName("조합 생성 - 성공")
    public void test_combination_create_success() throws Exception{

        CombinationCreateRequest request = 조합_생성_요청();
        CombinationCreateResponse response = 조합_생성_응답();

        when(combinationService
                .create(any(CombinationCreateRequest.class), any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/combination/create")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("combination/create/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        requestFields(
                                fieldWithPath("name").description("조합의 이름"),
                                fieldWithPath("icon").description("조합 아이콘 명칭"),
                                fieldWithPath("keywords").type(JsonFieldType.ARRAY).description("키워드 이름 리스트")),
                        responseFields(fieldWithPath("combinationId").description("생성된 조합 ID"))))
                .andExpect(status().isOk());

        verify(combinationService, times(1)).create(any(CombinationCreateRequest.class), any(LoginMember.class));
    }

    @Test
    @DisplayName("조합 조회 - 성공")
    public void test_combination_detail_success() throws Exception{

        CombinationDetailResponse response = 조합_조회_응답();
        when(combinationService.detail(any(Long.class))).thenReturn(response);

        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/combination/{combinationId}", 조합_ID)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("combination/detail/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(parameterWithName("combinationId").description("조회하려는 조합 ID")),
                        responseFields(
                                fieldWithPath("id").description("조합 ID"),
                                fieldWithPath("name").description("조합 이름"),
                                fieldWithPath("icon").description("조합 아이콘"),
                                fieldWithPath("categoryKeywords").type(JsonFieldType.OBJECT).description("카테고리 별 키워드 정보"),
                                fieldWithPath("categoryKeywords.purpose").type(JsonFieldType.ARRAY).description("목적 카테고리 리스트(필수)"),
                                fieldWithPath("categoryKeywords.menu").type(JsonFieldType.ARRAY).description("메뉴 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.theme").type(JsonFieldType.ARRAY).description("테마 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.facility").type(JsonFieldType.ARRAY).description("시설 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.atmosphere").type(JsonFieldType.ARRAY).description("분위기 카테고리 리스트(없으면 빈 리스트)"))))
                .andExpect(status().isOk());

        verify(combinationService, times(1)).detail(any(Long.class));
    }

}