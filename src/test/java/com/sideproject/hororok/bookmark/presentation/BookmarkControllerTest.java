package com.sideproject.hororok.bookmark.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.bookmark.dto.response.BookmarkFoldersResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.hororok.common.fixtures.BookmarkFixtures.북마크_저장_요청;
import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.*;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    @DisplayName("북마크 아이콘 저장 버튼 클릭 시 북마크의 리스트를 반환한다.")
    public void test_save_bookmark_get() throws Exception {

        BookmarkFoldersResponse response = 북마크_폴더_목록_응답();


        when(bookmarkFolderService.bookmarkFolders(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/bookmark/save")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/save-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("folderCount").description("폴더 개수"),
                                fieldWithPath("folders").description("폴더 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("folders[].folderId").description("폴더 ID"),
                                fieldWithPath("folders[].name").description("폴더 이름"),
                                fieldWithPath("folders[].color").description("폴더 색상"),
                                fieldWithPath("folders[].bookmarkCount").description("폴더 내 북마크 개수"),
                                fieldWithPath("folders[].visible").description("폴더 지도 노출 여부"),
                                fieldWithPath("folders[].defaultFolder").description("기본 폴더 여부"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("북마크 아이콘 저장 버튼 클릭 후 실제 저장 시 Post요청으로 저장 동작이 수행된다.")
    public void test_save_bookmark_post() throws Exception {

        BookmarkSaveRequest request = 북마크_저장_요청();

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
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("cafeId").description("저장하려는 카페 Id"),
                                fieldWithPath("folderId").description("저장하려는 폴더 ID"))
                        )
                )
                .andExpect(status().isNoContent());

        verify(bookmarkService).save(bookmarkSaveRequestCaptor.capture());
        BookmarkSaveRequest capturedRequest = bookmarkSaveRequestCaptor.getValue();

        assertThat(capturedRequest.getFolderId()).isEqualTo(request.getFolderId());
        assertThat(capturedRequest.getCafeId()).isEqualTo(request.getCafeId());
    }

//    @Test
//    @DisplayName("북마크 삭제 기능을 수행할 경우 삭제 후 noContent를 반환한다.")
//    public void test_delete_bookmark() throws Exception {
//
//        Long bookmarkId = 북마크_ID;
//
//        mockMvc.perform(
//                        delete("/api/bookmark/delete/{bookmarkId}", bookmarkId)
//                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//
//        verify(bookmarkService).delete(bookmarkIdCaptor.capture());
//
//        Long capturedBookmarkId = bookmarkIdCaptor.getValue();
//        assertThat(capturedBookmarkId).isEqualTo(bookmarkId);
//    }
}