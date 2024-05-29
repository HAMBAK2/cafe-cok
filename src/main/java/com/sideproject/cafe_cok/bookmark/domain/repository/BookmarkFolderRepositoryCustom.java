package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkCafeDto;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderCountDto;

import java.util.List;

public interface BookmarkFolderRepositoryCustom {

    List<BookmarkFolderCountDto> findBookmarkFolderCountDtoListByMemberId(final Long memberId);
}
