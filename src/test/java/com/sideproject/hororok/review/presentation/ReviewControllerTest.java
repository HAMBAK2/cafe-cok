package com.sideproject.hororok.review.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import com.sideproject.hororok.review.dto.response.ReviewCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_생성_요청;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_생성_응답;
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




}