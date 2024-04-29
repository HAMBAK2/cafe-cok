package com.sideproject.hororok.review.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPageProfileEditResponse;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import com.sideproject.hororok.review.dto.response.ReviewCreateResponse;
import com.sideproject.hororok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.hororok.review.dto.response.ReviewDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.MemberFixtures.맴버_닉네임;
import static com.sideproject.hororok.common.fixtures.MyPageFixtures.마이페이지_프로필_수정_응답;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ReviewControllerTest extends ControllerTest {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Test
    @DisplayName("리뷰를 생성하는 기능 - 성공")
    public void test_create_review_success() throws Exception{

        byte[] content = "Test file content".getBytes();
        MockMultipartFile file
                = new MockMultipartFile("files", "test.jpg", "image/jpeg", content);
        List<MultipartFile> files = Arrays.asList(file);

        byte[] requestContent = objectMapper.writeValueAsString(리뷰_생성_요청()).getBytes();
        MockMultipartFile request =
                new MockMultipartFile("request", "", "application/json", requestContent);
        ReviewCreateResponse response = 리뷰_생성_응답();

        when(reviewService.createReview(any(ReviewCreateRequest.class), any(LoginMember.class), eq(files)))
                .thenReturn(response);

        mockMvc.perform(
                        multipart(HttpMethod.POST,"/api/review/create")
                                .file(file)
                                .file(request)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andDo(document("review/create/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        requestParts(
                                partWithName("files").description("추가하려는 리뷰 이미지의 리스트"),
                                partWithName("request").description("파일을 제외한 리뷰 요청 객체")),
                        requestPartFields("request",
                                fieldWithPath("cafeId").description("카페 ID"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("specialNote").description("리뷰 특이사항"),
                                fieldWithPath("keywords").type(JsonFieldType.ARRAY).description("리뷰 키워드 이름 리스트"),
                                fieldWithPath("starRating").description("리뷰 별점")),
                        responseFields(fieldWithPath("reviewId").description("생성된 리뷰 ID"))))
                .andExpect(status().isOk());

        verify(reviewService, times(1))
                .createReview(any(ReviewCreateRequest.class), any(LoginMember.class), eq(files));
    }

    @Test
    @DisplayName("리뷰 삭제 기능 - 성공")
    public void test_review_delete_success() throws Exception{


        ReviewDeleteResponse response = 리뷰_삭제_응답();
        when(reviewService.delete(any(Long.class))).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/review/{reviewId}/delete", 리뷰_ID)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andDo(document("review/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(parameterWithName("reviewId").description("삭제할 리뷰의 ID")),
                        responseFields(fieldWithPath("reviewId").description("삭제된 폴더 ID"))))
                .andExpect(status().isOk());

        verify(reviewService, times(1)).delete(any(Long.class));
    }

    @Test
    @DisplayName("리뷰 상세 정보를 응답하는 기능 - 성공")
    public void test_review_detail_success() throws Exception{

        ReviewDetailResponse response = 리뷰_상세_응답();
        when(reviewService.detail(any(Long.class))).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/review/{reviewId}", 리뷰_ID)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andDo(document("review/detail/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(parameterWithName("reviewId").description("리뷰의 ID")),
                        responseFields(
                                fieldWithPath("cafeId").description("카페 ID"),
                                fieldWithPath("cafeName").description("카페 이름"),
                                fieldWithPath("reviewId").description("리뷰 ID"),
                                fieldWithPath("starRating").description("리뷰 별점"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("specialNote").description("리뷰 특이사항"),
                                fieldWithPath("images").type(JsonFieldType.ARRAY).description("리뷰 이미지 리스트"),
                                fieldWithPath("images[].id").description("리뷰 이미지 ID"),
                                fieldWithPath("images[].imageUrl").description("리뷰 이미지 URL"),
                                fieldWithPath("categoryKeywords").type(JsonFieldType.OBJECT).description("카테고리 별 키워드 정보"),
                                fieldWithPath("categoryKeywords.purpose").type(JsonFieldType.ARRAY).description("목적 카테고리 리스트(필수)"),
                                fieldWithPath("categoryKeywords.menu").type(JsonFieldType.ARRAY).description("메뉴 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.theme").type(JsonFieldType.ARRAY).description("테마 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.facility").type(JsonFieldType.ARRAY).description("시설 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.atmosphere").type(JsonFieldType.ARRAY).description("분위기 카테고리 리스트(없으면 빈 리스트)"))))
                .andExpect(status().isOk());

        verify(reviewService, times(1)).detail(any(Long.class));
    }




}