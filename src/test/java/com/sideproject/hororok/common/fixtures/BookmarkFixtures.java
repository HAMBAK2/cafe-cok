package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.favorite.domain.Bookmark;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;

public class BookmarkFixtures {

    public static final Long 폴더_즐겨찾기_개수 = 2L;


    public static Bookmark 즐겨찾기(final Cafe cafe, final BookmarkFolder bookmarkFolder) {
        return new Bookmark(cafe, bookmarkFolder);
    }
}
