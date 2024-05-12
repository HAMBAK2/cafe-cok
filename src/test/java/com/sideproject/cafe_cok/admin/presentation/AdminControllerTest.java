package com.sideproject.cafe_cok.admin.presentation;

import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.common.annotation.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.AdminFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends ControllerTest {

    @Test
    @DisplayName("카페를 저장하는 기능 - 성공")
    public void test_create_review_success() throws Exception{

        byte[] content = "Test file content".getBytes();
        MockMultipartFile mainImage = new MockMultipartFile(
                        "mainImage", "mainImage.jpg", "image/jpeg", content);
        MockMultipartFile otherImage = new MockMultipartFile(
                                "otherImages", "otherImage.jpg", "image/jpeg", content);
        List<MultipartFile> otherImages = Arrays.asList(otherImage);

        byte[] requestContent = objectMapper.writeValueAsString(카페_저장_요청()).getBytes();
        MockMultipartFile request =
                new MockMultipartFile("request", "", "application/json", requestContent);
        AdminCafeSaveResponse response = 카페_저장_응답();

        when(adminService.saveCafe(any(AdminCafeSaveRequest.class), any(MultipartFile.class), eq(otherImages)))
                .thenReturn(response);

        mockMvc.perform(
                        multipart(HttpMethod.POST,"/api/admin/cafe/save")
                                .file(request)
                                .file(mainImage)
                                .file(otherImage)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andDo(document("admin/cafe/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("request").description("파일을 제외한 리뷰 요청 객체"),
                                partWithName("mainImage").description("카페 대표 이미지(필수)"),
                                partWithName("otherImages").description("메인 이미지를 제외한 나머지 카페 이미지")),
                        requestPartFields("request",
                                fieldWithPath("name").description("카페 이름(필수)"),
                                fieldWithPath("roadAddress").description("카페 주소(필수)"),
                                fieldWithPath("mapx").description("카페 경도(필수)"),
                                fieldWithPath("mapy").description("카페 위도(필수)"),
                                fieldWithPath("telephone").description("카페 전화번호"),
                                fieldWithPath("menus").type(JsonFieldType.ARRAY).description("카페의 메뉴에 관한 정보"),
                                fieldWithPath("menus[].name").description("메뉴 이름(메뉴가 존재한다면 필수)"),
                                fieldWithPath("menus[].price").description("메뉴 가격(메뉴가 존재한다면 필수)"),
                                fieldWithPath("menus[].image").description("메뉴 이미지(필수X)")),
                        responseFields(
                                fieldWithPath("id").description("카페 ID"),
                                fieldWithPath("name").description("카페 이름"),
                                fieldWithPath("roadAddress").description("카페 주소"),
                                fieldWithPath("mapx").description("카페 경도"),
                                fieldWithPath("mapy").description("카페 위도"),
                                fieldWithPath("telephone").description("카페 전화번호"),
                                fieldWithPath("mainImage").description("카페 대표 이미지 URL"))))
                .andExpect(status().isOk());

    }

}