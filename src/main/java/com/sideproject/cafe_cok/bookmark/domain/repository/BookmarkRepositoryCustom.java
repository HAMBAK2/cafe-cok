package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<BookmarkIdDto> findBookmarkIdDtoListByCafeIdAndMemberId(final Long cafeId,
                                                                 final Long memberId);
}
