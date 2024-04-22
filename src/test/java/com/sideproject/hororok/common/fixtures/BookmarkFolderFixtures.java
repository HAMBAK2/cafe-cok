package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.bookmark.dto.BookmarkFolderDto;
import com.sideproject.hororok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.hororok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.hororok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.hororok.member.domain.Member;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.BookmarkFixtures.북마크_개수;


public class BookmarkFolderFixtures {

    public static final Long 폴더_개수1 = 1L;
    public static final Long 폴더_개수 = 2L;
    public static final Long 삭제후_폴더_개수 = 1L;
    public static final Integer 폴더_리스트_사이즈 = 1;
    public static final Integer 폴더_리스트_인덱스1 = 0;
    public static final Integer 폴더_리스트_인덱스2 = 1;
    public static final Long 폴더_ID_1 = 1L;
    public static final Long 폴더_ID_2 = 2L;

    /* 폴더1 */
    public static final String 즐겨찾기_폴더_이름1 = "즐겨찾기_폴더_이름1";
    public static final String 즐겨찾기_폴더_색상1 = "즐겨찾기_폴더_색상1";
    public static final Boolean 즐겨찾기_폴더_노출여부1 = true;
    public static final Boolean 즐겨찾기_폴더_디폴트여부1 = true;

    /* 폴더2 */
    public static final String 즐겨찾기_폴더_이름2 = "즐겨찾기_폴더_이름2";
    public static final String 즐겨찾기_폴더_색상2 = "즐겨찾기_폴더_색상2";
    public static final Boolean 즐겨찾기_폴더_노출여부2 = true;
    public static final Boolean 즐겨찾기_폴더_디폴트여부2 = false;


    public static BookmarkFolder 폴더1(final Member member) {

        return new BookmarkFolder(즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1,
                즐겨찾기_폴더_노출여부1, 즐겨찾기_폴더_디폴트여부1, member);
    }

    public static BookmarkFolder 폴더2(final Member member) {

        return new BookmarkFolder(즐겨찾기_폴더_이름2, 즐겨찾기_폴더_색상2,
                즐겨찾기_폴더_노출여부2, 즐겨찾기_폴더_디폴트여부2, member);
    }

    public static BookmarkFolderDto 폴더_Dto(
            final Long folderId, final String name,
            final String color, final Boolean isVisible,
            final Boolean isDefaultFolder){
        return BookmarkFolderDto.of(
                folderId, name, color, isVisible,
                isDefaultFolder, 북마크_개수);
    }

    public static BookmarkFoldersResponse 북마크_폴더_응답() {

        List<BookmarkFolderDto> folders = new ArrayList<>();
        BookmarkFolderDto bookmarkFolderDto1 =
                폴더_Dto(폴더_ID_1, 즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1,
                        즐겨찾기_폴더_노출여부1, 즐겨찾기_폴더_디폴트여부1);

        folders.add(bookmarkFolderDto1);

        return new BookmarkFoldersResponse(폴더_개수1, folders);
    }

    public static BookmarkFolderSaveRequest 폴더_저장_요청() {
        return new BookmarkFolderSaveRequest(
                즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1, 즐겨찾기_폴더_노출여부1);
    }

    public static BookmarkFolderUpdateRequest 폴더_수정_요청() {
        return new BookmarkFolderUpdateRequest(
                폴더_ID_1, 즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1, 즐겨찾기_폴더_노출여부1);
    }

    public static List<BookmarkFolderDto> 폴더_DTO_리스트() {
        List<BookmarkFolderDto> folderDtos = new ArrayList<>();
        BookmarkFolderDto folderDto = 폴더_Dto(폴더_ID_1, 즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1,
                즐겨찾기_폴더_노출여부1, 즐겨찾기_폴더_디폴트여부1);
        BookmarkFolderDto folderDto2 =
                폴더_Dto(폴더_ID_2, 즐겨찾기_폴더_이름2, 즐겨찾기_폴더_색상2,
                        즐겨찾기_폴더_노출여부2, 즐겨찾기_폴더_디폴트여부1);
        folderDtos.add(folderDto);
        folderDtos.add(folderDto2);

        return folderDtos;
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
