package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;

import java.util.List;

public interface BookmarkFolderRepositoryCustom {

    List<BookmarkFolderDetailDto> getBookmarkFolderDetails(final Long memberId);
}
