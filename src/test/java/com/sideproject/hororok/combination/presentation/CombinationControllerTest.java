package com.sideproject.hororok.combination.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.combination.dto.request.CombinationCreateRequest;
import com.sideproject.hororok.combination.dto.response.CombinationCreateResponse;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.hororok.common.fixtures.CombinationFixtures.조합_생성_요청;
import static com.sideproject.hororok.common.fixtures.CombinationFixtures.조합_생성_응답;
import static com.sideproject.hororok.common.fixtures.MyPageFixtures.마이페이지_프로필_응답;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

}