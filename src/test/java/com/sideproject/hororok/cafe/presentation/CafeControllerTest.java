package com.sideproject.hororok.cafe.presentation;

import com.sideproject.hororok.cafe.dto.response.CafeDetailTopResponse;
import com.sideproject.hororok.common.annotation.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페_상세_상단_응답;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페_아이디;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CafeControllerTest extends ControllerTest {

    @Test
    @DisplayName("카페 상세정보 상단 호출 - 성공")
    public void test_cafe_detail_top_success() throws Exception {

        CafeDetailTopResponse response = 카페_상세_상단_응답();

        when(cafeService
                .detailTop(any(Long.class)))
                .thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/cafe/{cafeId}/top", 카페_아이디)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("cafe/detail/top/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("cafeId").description("선택한 카페의 ID")),
                        responseFields(
                                fieldWithPath("cafeId").description("카페 ID"),
                                fieldWithPath("cafeName").description("카페 이름"),
                                fieldWithPath("roadAddress").description("카페 도로명 주소"),
                                fieldWithPath("starRating").description("카페 별점"),
                                fieldWithPath("reviewCount").description("카페의 리뷰 개수"),
                                fieldWithPath("keywords").type(JsonFieldType.ARRAY)
                                        .description("선택된 키워드 리스트\n\n" + "- 선택된 키워드 순 내림차순, 최대 3개 제공"),
                                fieldWithPath("keywords[].id").description("키워드 ID"),
                                fieldWithPath("keywords[].category").description("키워드 카테고리"),
                                fieldWithPath("keywords[].name").description("키워드 이름"))))
                .andExpect(status().isOk());

        verify(cafeService, times(1)).detailTop(any(Long.class));
    }

}