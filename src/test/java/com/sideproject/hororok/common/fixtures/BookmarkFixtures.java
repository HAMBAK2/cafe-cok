package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.favorite.domain.Bookmark;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.dto.BookmarkDto;
import com.sideproject.hororok.favorite.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.favorite.dto.response.BookmarksResponse;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.*;

public class BookmarkFixtures {

    public static final Long 북마크_개수 = 2L;
    public static final Long 북마크_ID = 1L;

    /* 북마크1 */


    public static Bookmark 북마크(final Cafe cafe, final BookmarkFolder bookmarkFolder) {
        return new Bookmark(cafe, bookmarkFolder);
    }

    public static BookmarkDto 북마크_DTO(final Cafe cafe) {
        return BookmarkDto.from(cafe);
    }

    public static BookmarksResponse 북마크_리스트_응답() {

        List<BookmarkDto> bookmarks = Arrays.asList(북마크_DTO(카페()), 북마크_DTO(카페2()));

        return new BookmarksResponse(폴더_ID_1, 즐겨찾기_폴더_이름1, 즐겨찾기_폴더_색상1, bookmarks);
    }

    public static BookmarkSaveRequest 북마크_저장_요청() {
        return new BookmarkSaveRequest(카페_아이디, 폴더_ID_1);
    }
}
