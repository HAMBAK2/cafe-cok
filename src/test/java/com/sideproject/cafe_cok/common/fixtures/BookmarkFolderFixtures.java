package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderDeleteResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFoldersResponse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.BookmarkFixtures.북마크_개수_1개;
import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;


public class BookmarkFolderFixtures {

    public static final Long 폴더_개수 = 1L;
    public static final Integer 폴더_리스트_사이즈 = 1;
    public static final Integer 폴더_리스트_인덱스 = 0;
    public static final Long 일반_폴더_ID = 1L;
    public static final Long 디폴트_폴더_ID = 2L;

    /* 폴더 */
    public static final String 폴더_이름 = "폴더_이름";
    public static final String 폴더_색상 = "폴더_색상";
    public static final Boolean 폴더_지도_노출_O = true;
    public static final Boolean 폴더_디폴트_X = false;

    public static BookmarkFolderSaveRequest 폴더_저장_요청() {
        return new BookmarkFolderSaveRequest(
                폴더_이름, 폴더_색상, 폴더_지도_노출_O);
    }

    public static BookmarkFolderUpdateRequest 일반_폴더_수정_요청() {
        return new BookmarkFolderUpdateRequest(
                일반_폴더_ID, 폴더_이름, 폴더_색상, 폴더_지도_노출_O);
    }

    public static BookmarkFolderUpdateRequest 디폴트_폴더_수정_요청() {
        return new BookmarkFolderUpdateRequest(
                디폴트_폴더_ID, 폴더_이름, 폴더_색상, 폴더_지도_노출_O);
    }

    public static BookmarkFoldersResponse 북마크_폴더_목록_응답() {
        return BookmarkFoldersResponse.of(폴더_개수, Arrays.asList(일반_폴더()));
    }

    public static BookmarkFolderDeleteResponse 북마크_폴더_삭제_응답(Long folderId) {
        return new BookmarkFolderDeleteResponse(folderId);
    }

    public static BookmarkFolderDto 일반_폴더_DTO() {
        return BookmarkFolderDto.from(일반_폴더());
    }

    public static List<BookmarkFolderDto> 일반_폴더_DTO_리스트() {
        return Arrays.asList(일반_폴더_DTO());
    }

    public static BookmarkFolder 일반_폴더() {
        BookmarkFolder folder = setId(new BookmarkFolder(폴더_이름, 폴더_색상, 폴더_지도_노출_O, 폴더_디폴트_X, 사용자()), 일반_폴더_ID);
        return folder;
    }

    public static BookmarkFolder setId(BookmarkFolder bookmarkFolder, final Long id) {

        try {
            Field idField = BookmarkFolder.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bookmarkFolder, id);
            return bookmarkFolder;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
