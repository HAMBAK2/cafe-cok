package com.sideproject.hororok.bookmark.presentation;

import com.sideproject.hororok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.bookmark.dto.request.BookmarkSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.sideproject.hororok.common.fixtures.BookmarkFixtures.*;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookmarkControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer JWT TOKEN";

    @Captor
    private ArgumentCaptor<BookmarkSaveRequest> bookmarkSaveRequestCaptor;

    @Captor
    private ArgumentCaptor<Long> bookmarkIdCaptor;

    @Test
    @DisplayName("북마크 아이콘 저장 버튼 클릭 후 실제 저장 시 Post요청으로 저장 동작이 수행된다.")
    public void test_save_bookmark_post() throws Exception {

        BookmarkSaveRequest request = 북마크_저장_요청();
        BookmarkIdResponse response = 북마크_ID_응답();

        when(bookmarkService.save(any(BookmarkSaveRequest.class))).thenReturn(response);

        mockMvc.perform(
                        post("/api/bookmark/save")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("bookmark/save-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(fieldWithPath("bookmarkId").description("저장한 북마크 ID")),
                        requestFields(
                                fieldWithPath("cafeId").description("저장하려는 카페 Id"),
                                fieldWithPath("folderId").description("저장하려는 폴더 ID"))))
                .andExpect(status().isOk());

        verify(bookmarkService).save(bookmarkSaveRequestCaptor.capture());
        BookmarkSaveRequest capturedRequest = bookmarkSaveRequestCaptor.getValue();

        assertThat(capturedRequest.getFolderId()).isEqualTo(request.getFolderId());
        assertThat(capturedRequest.getCafeId()).isEqualTo(request.getCafeId());
    }

    @Test
    @DisplayName("북마크 삭제 - 성공")
    public void test_delete_bookmark_success() throws Exception {

        Long bookmarkId = 북마크_ID;
        BookmarkIdResponse response = 북마크_ID_응답();

        when(bookmarkService.delete(any(Long.class))).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/bookmark/{bookmarkId}/delete", bookmarkId)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(fieldWithPath("bookmarkId").description("삭제한 북마크 ID")),
                        pathParameters(parameterWithName("bookmarkId").description("삭제할 북마크의 ID"))))
                .andExpect(status().isOk());


        verify(bookmarkService).delete(bookmarkIdCaptor.capture());

        Long capturedBookmarkId = bookmarkIdCaptor.getValue();
        assertThat(capturedBookmarkId).isEqualTo(bookmarkId);
    }
}