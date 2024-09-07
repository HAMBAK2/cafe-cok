package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<BookmarkFolderIdsDto> getBookmarkFolderIds(final Long cafeId,
                                                    final Long memberId);
}
