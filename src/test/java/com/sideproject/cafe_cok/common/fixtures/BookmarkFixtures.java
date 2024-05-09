package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkCafeDto;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkDto;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.BookmarkFolderFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.*;

public class BookmarkFixtures {

    public static final Long 북마크_개수_1개 = 1L;
    public static final Integer 북마크_리스트_사이즈_1개 = 1;
    public static final Integer 북마크_리스트_인덱스 = 0;
    public static final Long 북마크_ID = 1L;


    public static Bookmark 북마크() {
        Bookmark bookmark = new Bookmark(카페(), 일반_폴더());
        setId(bookmark, 북마크_ID);
        return bookmark;
    }

    public static BookmarkDto 북마크_DTO(final Bookmark bookmark) {
        return BookmarkDto.from(bookmark);
    }

    public static BookmarkCafeDto 북마크_카페_DTO(){
        return BookmarkCafeDto.from(북마크());
    }

    public static BookmarksResponse 북마크_리스트_응답() {

        List<BookmarkDto> bookmarks = Arrays.asList(북마크_DTO(북마크()));

        return new BookmarksResponse(일반_폴더_ID, 폴더_이름, 폴더_색상, bookmarks);
    }

    public static BookmarkSaveRequest 북마크_저장_요청() {
        return new BookmarkSaveRequest(카페_아이디, 일반_폴더_ID);
    }

    public static BookmarkIdResponse 북마크_ID_응답() {
        return BookmarkIdResponse.of(북마크_ID);
    }

    public static Bookmark setId(Bookmark bookmark, final Long id) {

        try {
            Field idField = Bookmark.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bookmark, id);
            return bookmark;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
