package com.sideproject.cafe_cok.bookmark.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderDeleteResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderDeletionNotAllowedException;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderUpdateNotAllowedException;
import com.sideproject.cafe_cok.common.annotation.ControllerTest;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.cafe_cok.common.fixtures.BookmarkFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static com.sideproject.cafe_cok.common.fixtures.BookmarkFolderFixtures.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class BookmarkFolderControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer JWT TOKEN";


    @Captor
    private ArgumentCaptor<Long> bookmarkFolderIdCaptor;

    @Captor
    private ArgumentCaptor<LoginMember> loginMemberCaptor;

    @Captor
    private ArgumentCaptor<BookmarkFolderUpdateRequest> bookmarkFolderUpdateRequestCaptor;

    @Captor
    private ArgumentCaptor<BookmarkFolderSaveRequest> bookmarkFolderSaveRequestCaptor;




    @Test
    @DisplayName("하단 탭의 저장을 눌렀을 시 - 성공")
    public void test_bookmark_folders_success() throws Exception {

        BookmarkFoldersResponse response = 북마크_폴더_목록_응답();

        when(bookmarkFolderService
                .bookmarkFolders(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/bookmark/folders")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/folders/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("folderCount").description("폴더 개수"),
                                fieldWithPath("folders").description("폴더 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("folders[].folderId").description("폴더 ID"),
                                fieldWithPath("folders[].name").description("폴더 이름"),
                                fieldWithPath("folders[].color").description("폴더 색상"),
                                fieldWithPath("folders[].bookmarkCount").description("폴더 내 북마크 개수"),
                                fieldWithPath("folders[].visible").description("폴더 지도 노출 여부"),
                                fieldWithPath("folders[].defaultFolder").description("기본 폴더 여부"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderCount").value(폴더_개수))
                .andExpect(jsonPath("$.folders").isArray())
                .andExpect(jsonPath("$.folders", hasSize(폴더_리스트_사이즈)))
                .andExpect(jsonPath("$.folders[" + 폴더_리스트_인덱스 + "].folderId").value(일반_폴더_ID));
    }

    @Test
    @DisplayName("북마크 폴더 저장 - 성공")
    public void test_folder_save_success() throws Exception {

        BookmarkFolderSaveRequest request = 폴더_저장_요청();

        //then
        mockMvc.perform(
                        post("/api/bookmark/folder/save")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("bookmark/folder/save/success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                                requestFields(
                                        fieldWithPath("name").description("저장 폴더 이름"),
                                        fieldWithPath("color").description("저장 폴더 색상"),
                                        fieldWithPath("isVisible").description("저장 지도 노출 여부"))))
                .andExpect(status().isNoContent());


        verify(bookmarkFolderService)
                .save(bookmarkFolderSaveRequestCaptor.capture(), loginMemberCaptor.capture());
        BookmarkFolderSaveRequest capturedRequest = bookmarkFolderSaveRequestCaptor.getValue();

        assertThat(capturedRequest.getName()).isEqualTo(request.getName());
        assertThat(capturedRequest.getColor()).isEqualTo(request.getColor());
        assertThat(capturedRequest.getIsVisible()).isEqualTo(request.getIsVisible());
    }

    @Test
    @DisplayName("북마크 폴더 수정 - 성공")
    public void test_folder_update_success() throws Exception {

        BookmarkFolderUpdateRequest request = 일반_폴더_수정_요청();

        //then
        mockMvc.perform(
                        put("/api/bookmark/folder/update")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("bookmark/folder/update/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        requestFields(
                                fieldWithPath("folderId").description("수정 폴더 ID"),
                                fieldWithPath("name").description("수정 폴더 이름"),
                                fieldWithPath("color").description("수정 폴더 색상"),
                                fieldWithPath("isVisible").description("수정 지도 노출 여부"))))
                .andExpect(status().isNoContent());

        verify(bookmarkFolderService)
                .update(bookmarkFolderUpdateRequestCaptor.capture());
        BookmarkFolderUpdateRequest capturedRequest = bookmarkFolderUpdateRequestCaptor.getValue();

        assertThat(capturedRequest.getFolderId()).isEqualTo(request.getFolderId());
        assertThat(capturedRequest.getName()).isEqualTo(request.getName());
        assertThat(capturedRequest.getColor()).isEqualTo(request.getColor());
        assertThat(capturedRequest.getIsVisible()).isEqualTo(request.getIsVisible());

    }

    @Test
    @DisplayName("북마크 폴더 수정 - 실패(디폴트 폴더의 이름/색상을 변경하려는 경우)")
    public void test_folder_update_fail_notPermissible() throws Exception {

        BookmarkFolderUpdateRequest request = 디폴트_폴더_수정_요청();

        doThrow(new DefaultFolderUpdateNotAllowedException())
                .when(bookmarkFolderService)
                .update(any(BookmarkFolderUpdateRequest.class));

        //then
        mockMvc.perform(
                        put("/api/bookmark/folder/update")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("bookmark/folder/update/fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                                requestFields(
                                        fieldWithPath("folderId").description("수정 폴더 ID"),
                                        fieldWithPath("name").description("수정 폴더 이름"),
                                        fieldWithPath("color").description("수정 폴더 색상"),
                                        fieldWithPath("isVisible").description("수정 지도 노출 여부")),
                                responseFields(
                                        fieldWithPath("message").description("에러 메시지"))))
                .andExpect(jsonPath("$.message").value("기본 폴더의 이름과 색상은 변경할 수 없습니다."))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("북마크 폴더 삭제 - 성공")
    public void test_folder_delete_success() throws Exception {

        Long folderId = 일반_폴더_ID;
        BookmarkFolderDeleteResponse response = 북마크_폴더_삭제_응답(일반_폴더_ID);

        when(bookmarkFolderService
                .delete(any(Long.class)))
                .thenReturn(response);


        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/bookmark/folder/{folderId}/delete", folderId)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/folder/delete/success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                                pathParameters(
                                        parameterWithName("folderId").description("삭제할 폴더의 ID")),
                                responseFields(
                                        fieldWithPath("folderId").description("삭제된 폴더 ID"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderId").value(일반_폴더_ID));

        verify(bookmarkFolderService)
                .delete(bookmarkFolderIdCaptor.capture());
        Long capturedFolderId = bookmarkFolderIdCaptor.getValue();
        assertThat(capturedFolderId).isEqualTo(folderId);
    }

    @Test
    @DisplayName("북마크 폴더 삭제 - 실패(디폴트 폴더를 삭제하려고 할 때)")
    public void test_folder_delete_fail_notPermissible() throws Exception {

        Long folderId = 디폴트_폴더_ID;

        doThrow(new DefaultFolderDeletionNotAllowedException())
                .when(bookmarkFolderService)
                .delete(any(Long.class));

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/bookmark/folder/{folderId}/delete", folderId)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/folder/delete/fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                                pathParameters(
                                        parameterWithName("folderId").description("삭제할 폴더의 ID")),
                                responseFields(
                                        fieldWithPath("message").description("에러 메시지"))))
                .andExpect(jsonPath("$.message").value("기본 폴더는 삭제할 수 없습니다."))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("북마크 폴더 지도 노출 여부를 변경 - 성공")
    public void test_update_folder_visible_success() throws Exception {

        Long folderId = 일반_폴더_ID;

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/api/bookmark/folder/{folderId}/update/visible", folderId)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/folder/update/visible/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(
                                parameterWithName("folderId").description("지도 노출여부를 수정할 폴더의 ID"))))
                .andExpect(status().isNoContent());


        verify(bookmarkFolderService)
                .updateFolderVisible(bookmarkFolderIdCaptor.capture());
        Long capturedBookmarkFolderId = bookmarkFolderIdCaptor.getValue();

        assertThat(capturedBookmarkFolderId).isEqualTo(folderId);

    }

    @Test
    @DisplayName("북마크 폴더 선택 시 해당 폴더의 북마크 리스트를 반환 - 성공")
    public void test_get_bookmarks_success() throws Exception {

        Long folderId = 일반_폴더_ID;
        BookmarksResponse response = 북마크_리스트_응답();

        when(bookmarkFolderService
                .bookmarks(any(Long.class)))
                .thenReturn(response);

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/bookmark/folder/{folderId}", folderId)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("bookmark/folder/choice/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(
                                parameterWithName("folderId").description("선택한 폴더의 ID")),

                        responseFields(
                                fieldWithPath("folderId").description("선택한 폴더의 ID"),
                                fieldWithPath("folderName").description("폴더 이름"),
                                fieldWithPath("folderColor").description("폴더 색상"),
                                fieldWithPath("bookmarks").description("북마크 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("bookmarks[].bookmarkId").description("북마크 ID"),
                                fieldWithPath("bookmarks[].cafeId").description("북마크에 추가할 카페 ID"),
                                fieldWithPath("bookmarks[].cafeName").description("카페 이름"),
                                fieldWithPath("bookmarks[].roadAddress").description("도로명 주소"),
                                fieldWithPath("bookmarks[].latitude").description("위도"),
                                fieldWithPath("bookmarks[].longitude").description("경도"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderId").value(folderId))
                .andExpect(jsonPath("$.folderName").value(response.getFolderName()))
                .andExpect(jsonPath("$.folderColor").value(response.getFolderColor()))
                .andExpect(jsonPath("$.bookmarks").isArray())
                .andExpect(jsonPath("$.bookmarks", hasSize(북마크_리스트_사이즈_1개)))
                .andExpect(jsonPath("$.bookmarks[" + 북마크_리스트_인덱스 + "].cafeId")
                        .value(response.getBookmarks().get(북마크_리스트_인덱스).getCafeId()));
    }



}