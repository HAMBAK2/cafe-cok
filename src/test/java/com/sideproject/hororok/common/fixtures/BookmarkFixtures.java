package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.bookmark.domain.Bookmark;
import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.bookmark.dto.BookmarkDto;
import com.sideproject.hororok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.bookmark.dto.response.BookmarksResponse;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.BookmarkFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.*;

public class BookmarkFixtures {

    public static final Long 북마크_개수_1개 = 1L;
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

        return new BookmarksResponse(일반_폴더_ID, 폴더_이름, 폴더_색상, bookmarks);
    }

    public static BookmarkSaveRequest 북마크_저장_요청() {
        return new BookmarkSaveRequest(카페_아이디, 일반_폴더_ID);
    }
}
